package cn.com.incardata.autobon_shops;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;

import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.utils.BaiduMapUtil;

/**
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
    protected ImageView iv_back,iv_clear;

    protected static Overlay markOverlay; //标志物图层
    protected static Overlay popOverlay;  //信息框图层
    protected static int defaultLevel = 15;
    protected static int scanTime = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cooperative_info_two_activity);
        init();
        initBaiduMapView();
    }

    protected void initBaiduMapView(){
        mMapView = (MapView) findViewById(R.id.bmapView);  	// 获取地图控件引用
        baiduMap = mMapView.getMap();  //管理具体的某一个MapView对象,缩放,旋转,平移
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(BaiduMapUtil.defaultLevel);  //默认级别12
        baiduMap.setMapStatus(mapStatusUpdate);  //设置缩放级别
        mLocationClient = new LocationClient(this);

        BaiduMapUtil.hiddenBaiduLogo(mMapView);  //隐藏百度广告图标
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(true);  //默认是true,显示标尺

        /**
         * 百度地图加载完毕后回调此方法(传参入口)
         */
        baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                registerMyLocation();
                Bundle bundle = getIntent().getExtras();
                Log.i("test",bundle.getString("company")+"===>"+bundle.getString("card_number")
                        +"===>"+bundle.getString("business_pic_url")+"===>"+bundle.getString("corporation_pic_url"));
            }
        });
    }

    private void init(){
        context = this;
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        et_content = (EditText) findViewById(R.id.et_content);
        iv_back.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
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
        }
    }

    public void registerMyLocation(){
        myBDLocationListener = new MyBDLocationListener();
        mLocationClient.registerLocationListener(myBDLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(scanTime);  //设置扫描定位时间
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);
        option.setOpenGps(true);  //设置打开GPS
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        baiduMap.setMyLocationEnabled(true);// 打开定位图层
        baiduMap.getUiSettings().setCompassEnabled(false);  //不显示指南针
        MyLocationConfiguration configuration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true,
                BitmapDescriptorFactory.fromResource(R.mipmap.shop));
        baiduMap.setMyLocationConfigeration(configuration);// 设置定位显示的模式
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(baiduMap.getMapStatus().zoom));  //定位后更新缩放级别
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
        if(mLocationClient!=null){
            mLocationClient.stop();
            baiduMap.setMyLocationEnabled(false); //关闭定位图层
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myBDLocationListener!=null){
            mLocationClient.unRegisterLocationListener(myBDLocationListener);
            myBDLocationListener = null;
        }
        mLocationClient = null;
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
                    popOverlay = BaiduMapUtil.drawPopWindow(baiduMap,context,latLng,address,BaiduMapUtil.popZIndex);
                    BaiduMapUtil.zoomByOneCenterPoint(baiduMap,latLng,defaultLevel);
                }
            }
        }
    }
}
