package cn.com.incardata.autobon_shops;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;

import cn.com.incardata.fragment.TechnicianDialogFragment;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.GetTechnicianEntity;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.RevokeOrderEntity;
import cn.com.incardata.http.response.TechnicianMessage;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.BaiduMapUtil;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.CaptureDialog;

/** 未完成订单详情 二期
 * Created by yang on 2016/11/23.
 */
public class UnFinishOrderInfoActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private TextView order_num,orderStatus,orderType,agreeStartTime,agreeEndTime,createOrderTime,orderRemark;
    private Button appoint_tech,revoke_order;
    private GridView order_grid,before_photo;
    protected BaiduMap baiduMap;
    private MapView mMapView;
    private OrderInfo orderInfo;
    protected Overlay[] markOverlay;
    protected static int defaultLevel = 16;
    private LatLng coopLatlng,techLatlng;
    private LinearLayout ll1;
    private ImageView img_tech_message,back;
    private TechnicianMessage technicianMessage;
    private CaptureDialog captureDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unfinish_order_info);
        initView();

    }

    public void initView(){
        orderInfo = getIntent().getParcelableExtra("order_info");
        if (orderInfo.getTechName() != null){
            getTechMessage();
        }
        context = this;
        markOverlay = new Overlay[2];
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        order_num = (TextView) findViewById(R.id.order_num);
        orderStatus = (TextView) findViewById(R.id.orderStatus);
        orderType = (TextView) findViewById(R.id.orderType);
        agreeStartTime = (TextView) findViewById(R.id.agreeStartTime);
        agreeEndTime = (TextView) findViewById(R.id.agreeEndTime);
        createOrderTime = (TextView) findViewById(R.id.createOrderTime);
        orderRemark = (TextView) findViewById(R.id.orderRemark);
        order_grid = (GridView) findViewById(R.id.order_grid);
        before_photo = (GridView) findViewById(R.id.before_photo);
        appoint_tech = (Button) findViewById(R.id.appoint_tech);
        revoke_order = (Button) findViewById(R.id.revoke_order);
        img_tech_message = (ImageView) findViewById(R.id.img_tech_message);
        back = (ImageView) findViewById(R.id.back);
        mMapView = (MapView) findViewById(R.id.bmapView);

        order_grid.setFocusable(false);
        before_photo.setFocusable(false);

        appoint_tech.setOnClickListener(this);
        revoke_order.setOnClickListener(this);
        img_tech_message.setOnClickListener(this);
        back.setOnClickListener(this);
        ll1.setOnClickListener(this);

        if (orderInfo != null){
            bindData();
            initBaiduMapView();
        }else {
            T.show(context,"数据加载失败");
        }

    }
    public void initBaiduMapView(){
        coopLatlng = new LatLng(Double.parseDouble(orderInfo.getLatitude()),Double.parseDouble(orderInfo.getLongitude()));
        if (orderInfo.getTechLatitude() == null || orderInfo.getTechLongitude() == null){
        }else {
            techLatlng = new LatLng(Double.parseDouble(orderInfo.getTechLatitude()),Double.parseDouble(orderInfo.getTechLongitude()));
        }

        baiduMap = mMapView.getMap();
        /**
         * 百度地图加载完毕后回调此方法(传参入口)
         */
        baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (markOverlay[0] != null) {
                    markOverlay[0].remove();
                }
                if (markOverlay[1] != null) {
                    markOverlay[1].remove();
                }
                if (!NetWorkHelper.isNetworkAvailable(context)) { //无网络不显示
                    return;
                }
                if (orderInfo.getTechLatitude() == null || orderInfo.getTechLongitude() == null){
                    T.show(getContext(),"暂无技师位置信息");
                    markOverlay[0] = BaiduMapUtil.drawMarker(baiduMap,coopLatlng, BitmapDescriptorFactory.fromResource(R.mipmap.coop),BaiduMapUtil.markZIndex);
                    BaiduMapUtil.zoomByOneCenterPoint(baiduMap, coopLatlng, 16);
                }else {
                    markOverlay[0] = BaiduMapUtil.drawMarker(baiduMap,coopLatlng, BitmapDescriptorFactory.fromResource(R.mipmap.coop),BaiduMapUtil.markZIndex);
                    markOverlay[1] = BaiduMapUtil.drawMarker(baiduMap,techLatlng, BitmapDescriptorFactory.fromResource(R.mipmap.tech),BaiduMapUtil.markZIndex);
//        popOverlay = BaiduMapUtil.drawPopWindow(baiduMap,context,mLatlng,address,BaiduMapUtil.popZIndex);
                    BaiduMapUtil.zoomByTwoPoint(baiduMap, coopLatlng, techLatlng);
                }
            }

        });


//
//        if(markOverlay[0]!=null){
//            markOverlay[0].remove();
//        }
//        if(markOverlay[1]!=null){
//            markOverlay[1].remove();
//        }
////        if(popOverlay!=null){
////            popOverlay.remove();
////        }
//        if(!NetWorkHelper.isNetworkAvailable(context)) { //无网络不显示
//            return;
//        }
////        if (orderInfo.getTechLatitude() == null || orderInfo.getTechLongitude() == null){
////            markOverlay[0] = BaiduMapUtil.drawMarker(baiduMap,coopLatlng, BitmapDescriptorFactory.fromResource(R.mipmap.shop),BaiduMapUtil.markZIndex);
////            BaiduMapUtil.zoomByOneCenterPoint(baiduMap, coopLatlng, 16);
////        }else {
//            markOverlay[0] = BaiduMapUtil.drawMarker(baiduMap,coopLatlng, BitmapDescriptorFactory.fromResource(R.mipmap.shop),BaiduMapUtil.markZIndex);
//            markOverlay[1] = BaiduMapUtil.drawMarker(baiduMap,techLatlng, BitmapDescriptorFactory.fromResource(R.mipmap.here),BaiduMapUtil.markZIndex);
////        popOverlay = BaiduMapUtil.drawPopWindow(baiduMap,context,mLatlng,address,BaiduMapUtil.popZIndex);
//            BaiduMapUtil.zoomByTwoPoint(baiduMap, coopLatlng, techLatlng);
//        }
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(true);
    }

    public void bindData(){
        order_num.setText("订单编号：" + orderInfo.getOrderNum());
        if ("CREATED_TO_APPOINT".equals(orderInfo.getStatus())) {//新建订单，待指定技师
            appoint_tech.setVisibility(View.VISIBLE);
            orderStatus.setTextColor(context.getResources().getColor(R.color.gray_A3));
            orderStatus.setText("待指派");
            ll1.setVisibility(View.GONE);
            mMapView.setVisibility(View.GONE);
        } else {
            appoint_tech.setVisibility(View.GONE);
            if ("NEWLY_CREATED".equals(orderInfo.getStatus())){
                orderStatus.setTextColor(context.getResources().getColor(R.color.gray_A3));
                orderStatus.setText("未接单");
                ll1.setVisibility(View.GONE);
                mMapView.setVisibility(View.GONE);
            }
            else if ("TAKEN_UP".equals(orderInfo.getStatus())) {
                orderStatus.setTextColor(context.getResources().getColor(R.color.darkgray));
                orderStatus.setText("已接单");
            } else if ("IN_PROGRESS".equals(orderInfo.getStatus())) {
                orderStatus.setTextColor(context.getResources().getColor(R.color.main_orange));
                orderStatus.setText("已出发");
            } else if ("SIGNED_IN".equals(orderInfo.getStatus())) {
                orderStatus.setTextColor(context.getResources().getColor(R.color.main_orange));
                orderStatus.setText("已签到");
            } else if ("AT_WORK".equals(orderInfo.getStatus())) {
                orderStatus.setTextColor(context.getResources().getColor(R.color.main_orange));
                long useTime = 0L;
                try {
                    long currentTime = System.currentTimeMillis();
                    long startWorkTime = orderInfo.getStartTime();
                    useTime = currentTime - startWorkTime;
                    Log.i("test", "currentTime===>" + currentTime + ",useTime===>" + useTime);
                } catch (Exception e) {
                    useTime = 0L;
                    e.printStackTrace();
                }
                final int hour = (int) (useTime / (1000 * 3600));
                final int minute = (int) ((useTime - hour * (1000 * 3600)) / (1000 * 60));
                final int second = (int) ((useTime - hour * (1000 * 3600) - minute * (1000 * 60)) / 1000);
                if (hour <= 0){
                    if (minute <= 0){
                        orderStatus.setText("已施工1分钟");
                    }else {
                        orderStatus.setText("已施工" + minute + "分钟");
                    }
                }else {
                    orderStatus.setText("已施工" + hour + "小时" + minute + "分钟");
                }
            }
        }
        if (orderInfo.getType() == null){
            orderType.setText("");
        }else {
            String[] types = orderInfo.getType().split(",");
            String type = "";
            for (int i = 0; i < types.length; i++) {
                type = type + getProjectName(types[i]) + ",";
            }
            if (type != "" && type.length() > 0){
                type = type.substring(0,type.length() - 1);
            }
            orderType.setText(type);
        }
        agreeStartTime.setText(DateCompute.getDate(orderInfo.getAgreedStartTime()));
        agreeEndTime.setText(DateCompute.getDate(orderInfo.getAgreedEndTime()));
        createOrderTime.setText(DateCompute.getDate(orderInfo.getCreateTime()));
        orderRemark.setText(orderInfo.getRemark());

        Myadapter myadapter;
        final String[] orderPhotos;
        String urlOrderPhotos = orderInfo.getPhoto();
        if (urlOrderPhotos.contains(",")) {
            orderPhotos = urlOrderPhotos.split(",");
        } else {
            orderPhotos = new String[]{urlOrderPhotos};
        }

        myadapter = new Myadapter(this, orderPhotos);
        order_grid.setAdapter(myadapter);

        order_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                openImage(position, orderPhotos);
            }
        });

        if (orderInfo.getBeforePhotos() != null){
            final String[] urlBefore;
            String urlBeforePhotos = orderInfo.getBeforePhotos();
            if (urlBeforePhotos.contains(",")) {
                urlBefore = urlBeforePhotos.split(",");
            } else {
                urlBefore = new String[]{urlBeforePhotos};
            }
            myadapter = new Myadapter(this, urlBefore);
            before_photo.setAdapter(myadapter);

            before_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    openImage(position, urlBefore);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.img_tech_message:
                Intent intent1 = new Intent(context,TechnicianInfoActivity.class);
                intent1.putExtra("activityId",4);
                intent1.putExtra(AutoCon.ORDER_ID,orderInfo.getId());
                intent1.putExtra("techMessage",technicianMessage);
                startActivity(intent1);
                break;
            case R.id.appoint_tech:
                Intent intent = new Intent(getContext(), AddContactActivity.class);
                intent.putExtra(AutoCon.ORDER_ID, orderInfo.getId());
                intent.putExtra("activityId",3);
                startActivity(intent);
                break;
            case R.id.revoke_order:
                captureDialog = new CaptureDialog(getContext(), "确定撤销该订单！", true, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        captureDialog.dismiss();
                        revokeOrder();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        captureDialog.dismiss();
                    }
                });
                captureDialog.show();
                break;
            case R.id.ll1:
                Intent intent3 = new Intent(context,TechnicianInfoActivity.class);
                intent3.putExtra("activityId",4);
                intent3.putExtra(AutoCon.ORDER_ID,orderInfo.getId());
                intent3.putExtra("techMessage",technicianMessage);
                startActivity(intent3);
                break;
        }
    }
    public String getProjectName(String type) {
        if ("1".equals(type)) {
            return "隔热膜";
        } else if ("2".equals(type)) {
            return "隐形车衣";
        } else if ("3".equals(type)) {
            return "车身改色";
        } else if ("4".equals(type)) {
            return "美容清洁";
        }
        if (type == null) ;
        return null;
    }

    /**
     * 撤单
     *
     */
    private void revokeOrder(){
        Http.getInstance().putTaskToken(NetURL.getRevokeOrderV2(orderInfo.getId()), "", RevokeOrderEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null){
                    T.show(getContext(), R.string.operate_failed_agen);
                    return;
                }
                if (entity instanceof RevokeOrderEntity){
                    if (((RevokeOrderEntity) entity).isStatus()){
                       finish();
                        T.show(getContext(), getString(R.string.revoke_order_succeed));
                    }else {
                        T.show(getContext(), ((RevokeOrderEntity) entity).getMessage());
                    }
                }
            }
        });
    }

    /** 查看图片
     * @param position
     * @param urls
     */
    private void openImage(int position, String... urls){
        Bundle bundle = new Bundle();
        bundle.putStringArray(EnlargementActivity.IMAGE_URL, urls);
        bundle.putInt(EnlargementActivity.POSITION, position);
        startActivity(EnlargementActivity.class, bundle);
    }

    class Myadapter extends BaseAdapter {
        private Context context;
        private String[] urlItem;

        public Myadapter(Context context, String[] urlItem) {
            this.context = context;
            this.urlItem = urlItem;
        }

        @Override
        public int getCount() {
            return urlItem.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ImageView imageView;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.grid_item_image, viewGroup, false);
                imageView = (ImageView) view.findViewById(R.id.imgGridItem);
//                imageView.setLayoutParams(new GridView.LayoutParams(display.getWidth() / 3,display.getWidth() / 3));
//                GridView.LayoutParams params = new GridView.LayoutParams(display.getWidth() / 3, display.getWidth() / 3);
//                view.setLayoutParams(params);
                view.setTag(imageView);
            } else {
                imageView = (ImageView) view.getTag();
            }
            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + urlItem[position], imageView, false);


            return view;
        }
    }

    /**
     * 显示技师信息对话框
     */
    private void getTechMessage() {
        showDialog();
        Http.getInstance().getTaskToken(NetURL.getTechIdMessageV2(orderInfo.getTechId()), "" , GetTechnicianEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity != null && entity instanceof GetTechnicianEntity){
                    GetTechnicianEntity tech = (GetTechnicianEntity) entity;
                    if (tech.isStatus()){
                        technicianMessage = JSON.parseObject(tech.getMessage().toString(),TechnicianMessage.class);
                    }else {
                        T.show(getContext(),tech.getMessage().toString());
                    }
                }else {
                    T.show(getContext(), R.string.loading_data_failed);
                }
            }
        });
    }
}
