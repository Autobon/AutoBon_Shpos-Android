package cn.com.incardata.autobon_shops;

import android.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.response.CooperativeInfo_Data;
import cn.com.incardata.permission.PermissionUtil;
import cn.com.incardata.utils.BaiduMapUtil;
import cn.com.incardata.utils.T;

/** 二期弃用
 * Created by zhangming on 2016/3/30.
 * 合作商加盟
 */
public class CooperativeTwoActivity extends BaseActivity implements View.OnClickListener{
    protected Context context;
    protected EditText et_content;
    protected BaiduMap baiduMap;
    protected MapView mMapView;
    protected LocationClient mLocationClient;
    protected MyBDLocationListener myBDLocationListener;
    private MyGeoCoderListener geoCoderListener;
    protected ImageView iv_back,iv_clear;
    protected TextView tv_search;
    protected Button sure_btn;
    protected Overlay markOverlay; //标志物图层
    protected Overlay popOverlay;  //信息框图层

    protected static int defaultLevel = 16;
    protected static int scanTime = 1;

    private CooperativeInfo_Data extraCooperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cooperative_info_two_activity);
        //在使用百度地图SDK各组件之前初始化context信息,传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        checkPermission();
        init();
        initBaiduMapView();
    }

    /**
     * 申请存储权限
     *
     * @param commandCode 可选指令码，用以执行指定操作
     */
    private void checkPermission(final int... commandCode) {
        permissionUtil = new PermissionUtil(this);
        permissionUtil.requestPermissions(getString(R.string.please_grant_file_location_phone_correation_authority), new PermissionUtil.PermissionListener() {
                    @Override
                    public void doAfterGrant(String... permission) {
                        Log.d("Maintivity",getString(R.string.authorization_success));
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        Log.d("Maintivity",getString(R.string.authorization_fail));
                    }
                }, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initNetManager();
    }
    protected void initBaiduMapView(){
        mMapView = (MapView) findViewById(R.id.bmapView);  	// 获取地图控件引用
        baiduMap = mMapView.getMap();  //管理具体的某一个MapView对象,缩放,旋转,平移

        geoCoderListener = new MyGeoCoderListener();
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //获取经纬度
                System.out.println("latitude=" + latLng.longitude + ",longitude=" + latLng.latitude);
                // 构建MarkerOption，用于在地图上添加Marker
                showAddressInMap(latLng, "");

                //实例化一个地理编码查询对象
                GeoCoder geoCoder = GeoCoder.newInstance();
                //设置反地理编码位置坐标
                ReverseGeoCodeOption op = new ReverseGeoCodeOption();
                op.location(latLng);
                //发起反地理编码请求(经纬度->地址信息)
                geoCoder.reverseGeoCode(op);
                geoCoder.setOnGetGeoCodeResultListener(geoCoderListener);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                if (mapPoi != null){
                    // 构建MarkerOption，用于在地图上添加Marker
                    showAddressInMap(mapPoi.getPosition(), "");

                    //实例化一个地理编码查询对象
                    GeoCoder geoCoder = GeoCoder.newInstance();
                    //设置反地理编码位置坐标
                    ReverseGeoCodeOption op = new ReverseGeoCodeOption();
                    op.location(mapPoi.getPosition());
                    //发起反地理编码请求(经纬度->地址信息)
                    geoCoder.reverseGeoCode(op);
                    geoCoder.setOnGetGeoCodeResultListener(geoCoderListener);
                }
                return true;
            }
        });
        /**
         * 百度地图加载完毕后回调此方法(传参入口)
         */
        baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                registerMyLocation();
            }
        });
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(true);  //默认是true,显示标尺
    }

    private void initNetManager(){
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    private void init(){
        context = this;
        extraCooperation = getIntent().getExtras().getParcelable(MainReviewActivity.Cooperation);  //得到上一个页面传递的bundle

        sure_btn = (Button) findViewById(R.id.sure_btn);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        et_content = (EditText) findViewById(R.id.et_content);
        tv_search = (TextView) findViewById(R.id.tv_search);

        sure_btn.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
        tv_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear:
                et_content.setText("");
                break;
            case R.id.tv_search:
                String address = et_content.getText().toString().trim();
                addressToGeoPoint(address);
                break;
            case R.id.sure_btn:
                if (TextUtils.isEmpty(extraCooperation.getProvince())){
                    T.show(getContext(), getString(R.string.please_shops_location));
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putParcelable(MainReviewActivity.Cooperation, extraCooperation);
                startActivity(CooperativeThreeActivity.class, bundle);
                break;
        }
    }

    protected void addressToGeoPoint(String address) {
        GeoCoder mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(geoCoderListener);
        mSearch.geocode(new GeoCodeOption().city(address).address(address));
    }

    /**
     * 通过经纬度显示标注在地图上
     * param mLatlng
     */
    private void showAddressInMap(LatLng mLatlng,String address){
        if(markOverlay!=null){
            markOverlay.remove();
        }
        if(popOverlay!=null){
            popOverlay.remove();
        }
        if(!NetWorkHelper.isNetworkAvailable(context)) { //无网络不显示
            return;
        }
        markOverlay = BaiduMapUtil.drawMarker(baiduMap,mLatlng,BitmapDescriptorFactory.fromResource(R.mipmap.shop),BaiduMapUtil.markZIndex);
//        popOverlay = BaiduMapUtil.drawPopWindow(baiduMap,context,mLatlng,address,BaiduMapUtil.popZIndex);
        BaiduMapUtil.zoomByOneCenterPoint(baiduMap, mLatlng, defaultLevel);
    }

    public void registerMyLocation(){
        myBDLocationListener = new MyBDLocationListener();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myBDLocationListener);
        baiduMap.setMyLocationEnabled(true);// 打开定位图层

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(scanTime);  //设置扫描定位时间
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);
        option.setOpenGps(true);  //设置打开GPS
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mLocationClient.requestLocation();

        baiduMap.getUiSettings().setCompassEnabled(false);  //不显示指南针
//        MyLocationConfiguration configuration = new MyLocationConfiguration(
//                MyLocationConfiguration.LocationMode.FOLLOWING, true,
//                BitmapDescriptorFactory.fromResource(R.mipmap.shop));
//        baiduMap.setMyLocationConfigeration(configuration);// 设置定位显示的模式
//        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(baiduMap.getMapStatus().zoom));  //定位后更新缩放级别
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
        BaiduMapUtil.closeLocationClient(baiduMap,mLocationClient);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myBDLocationListener!=null){
            mLocationClient.unRegisterLocationListener(myBDLocationListener);
            myBDLocationListener = null;
        }
        if(markOverlay!=null){  //每次退出时销毁地点标记
            markOverlay = null;
        }
        if(popOverlay!=null){
            popOverlay = null;
        }
        mLocationClient = null;
        mMapView.onDestroy();
    }

    /**
     * 定位监听类
     */
    class MyBDLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation result) {
            if (result != null) {
                final double latitude = result.getLatitude();
                final double longitude = result.getLongitude();
                final LatLng latLng = new LatLng(latitude, longitude);
                String address = result.getAddrStr();

                if(markOverlay!=null){
                    View pop = BaiduMapUtil.initPop(context,null,false);
                    TextView tv = (TextView) pop.findViewById(R.id.title);
                    tv.setText(address);
                }else{
                    if(!NetWorkHelper.isNetworkAvailable(context)) {  //无网络不显示
                        return;
                    }
                    markOverlay = BaiduMapUtil.drawMarker(baiduMap,latLng, BitmapDescriptorFactory.fromResource(R.mipmap.shop),BaiduMapUtil.markZIndex);
//                    popOverlay = BaiduMapUtil.drawPopWindow(baiduMap,context,latLng,address,BaiduMapUtil.popZIndex);
                    BaiduMapUtil.zoomByOneCenterPoint(baiduMap,latLng,defaultLevel);
                }
            }
        }
    }

    class MyGeoCoderListener implements OnGetGeoCoderResultListener {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                T.show(context,getString(R.string.no_result_tips));  //没有检索到结果
                return;
            }
            LatLng mLatlng = result.getLocation();
            String address = result.getAddress();
            showAddressInMap(mLatlng,address);  //根据经纬度设置图标在地图上

            //实例化一个地理编码查询对象
            GeoCoder geoCoder = GeoCoder.newInstance();
            //设置反地理编码位置坐标
            ReverseGeoCodeOption op = new ReverseGeoCodeOption();
            op.location(mLatlng);
            //发起反地理编码请求(经纬度->地址信息)
            geoCoder.reverseGeoCode(op);
            geoCoder.setOnGetGeoCodeResultListener(geoCoderListener);
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result != null && !TextUtils.isEmpty(result.getAddress())){
                et_content.setText(result.getAddress());

                extraCooperation.setLongitude(String.valueOf(result.getLocation().longitude));
                extraCooperation.setLatitude(String.valueOf(result.getLocation().latitude));
                extraCooperation.setProvince(result.getAddressDetail().province);
                extraCooperation.setCity(result.getAddressDetail().city);
                extraCooperation.setDistrict(result.getAddressDetail().district);
            }
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d("test", "网络状态已经改变");
                if(NetWorkHelper.isNetworkAvailable(context)){
                    BaiduMapUtil.locate(baiduMap);
                }else{
                    T.show(context,context.getString(R.string.network_exception));
                    return;
                }
            }
        }
    };
}
