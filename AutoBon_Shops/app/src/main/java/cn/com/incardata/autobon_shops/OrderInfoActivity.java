package cn.com.incardata.autobon_shops;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.BaseEntity;
import cn.com.incardata.http.response.BaseEntityTwo;
import cn.com.incardata.http.response.Construction;
import cn.com.incardata.http.response.GetTechnicianEntity;
import cn.com.incardata.http.response.ListUnfinishedEntity;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.OrderWorkInfo;
import cn.com.incardata.http.response.TechnicianMessage;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.DecimalUtil;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.CircleImageView;
import cn.com.incardata.view.MyGridView;
import cn.com.incardata.view.OrderWorkTimeInfoPopupWindow;

/**
 * 订单详情
 */
public class OrderInfoActivity extends BaseForBroadcastActivity {
    private static final int RequestCode = 0x10;
    private TextView orderType;
    private Button commentStatus;
    private TextView orderNum;
    private TextView orderTime;
    private TextView startTime;
    private TextView endTime;
    private TextView agreeEndTime;
    //    private ImageView orderImage;
    private TextView license;
    private TextView vin;
    private TextView remark;
    private TextView createTime;
    //    private TextView workItem;
    private TextView workPerson;
    private GridView beforePhoto;
    private GridView afterPhoto;
    private GridView order_grid;
    private CircleImageView userPhoto;
    private TextView userName,user_phone;
    private TextView orderCount;
    private RatingBar ratingBar;
    //    private String[] workItems;
    private OrderInfo orderInfo;

    private Button collection;


    private List<OrderWorkInfo> orderWorkInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        initView();

    }

    private void initView() {
        orderType = (TextView) findViewById(R.id.order_type);
        commentStatus = (Button) findViewById(R.id.comment_state);
        orderNum = (TextView) findViewById(R.id.order_number);
        orderTime = (TextView) findViewById(R.id.order_time);
        startTime = (TextView) findViewById(R.id.work_startTime);
        endTime = (TextView) findViewById(R.id.work_endTime);
        agreeEndTime = (TextView) findViewById(R.id.agree_work_endTime);
        license = (TextView) findViewById(R.id.license);
        vin = (TextView) findViewById(R.id.vin);
        remark = (TextView) findViewById(R.id.remark);
        createTime = (TextView) findViewById(R.id.create_time);
        workPerson = (TextView) findViewById(R.id.work_person);
        beforePhoto = (MyGridView) findViewById(R.id.work_before_grid);
        afterPhoto = (GridView) findViewById(R.id.work_after_grid);
        order_grid = (GridView) findViewById(R.id.order_grid);
        userPhoto = (CircleImageView) findViewById(R.id.tech_header);
        userName = (TextView) findViewById(R.id.user_name);
        user_phone = (TextView) findViewById(R.id.user_phone);
        orderCount = (TextView) findViewById(R.id.order_num);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);

        collection = (Button) findViewById(R.id.collection);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.ll_check_work_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWorkTimePopupWindow();
            }
        });
//        orderImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                if (orderInfo != null){
//                    bundle.putStringArray(EnlargementActivity.IMAGE_URL, new String[]{orderInfo.getPhoto()});
//                }
//                startActivity(EnlargementActivity.class, bundle);
//            }
//        });

        commentStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("FINISHED".equals(orderInfo.getStatus())) {//未评价
                    Intent intent = new Intent(getContext(), GoCommentActivity.class);
                    intent.putExtra("UserName", orderInfo.getTechName());
                    intent.putExtra("UserPhotoUrl", orderInfo.getTechAvatar());
                    intent.putExtra("orderCount", orderInfo.getOrderCount());
                    intent.putExtra("OrderId", orderInfo.getId());
                    intent.putExtra("evaluate", orderInfo.getEvaluate());
                    startActivityForResult(intent, RequestCode);//去评价
                } else {
                    T.show(getContext(),"该订单已评价");
                    return;
                }

            }
        });

        user_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + orderInfo.getTechPhone());
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }
        });

        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectTechnician();
            }
        });

        orderInfo = getIntent().getParcelableExtra(AutoCon.ORDER_INFO);
        if (orderInfo != null){
            updateUI();
            getOrderWorkInfo();
        }else {
            T.show(getContext(),"数据加载失败");
        }
//        getTechInfo(orderInfo.getId());
    }

    OrderWorkTimeInfoPopupWindow pop;

    /**
     * 施工时间详情弹框
     */
    public void showWorkTimePopupWindow() {
        if (pop == null) {
            pop = new OrderWorkTimeInfoPopupWindow(this);
            pop.init();
        }
        pop.setData(orderWorkInfos);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 获取订单施工详情
     */
    private void getOrderWorkInfo(){
        showDialog();
        Http.getInstance().getTaskToken(NetURL.getOrderWorkInfo(orderInfo.getId()), "", ListUnfinishedEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity != null && entity instanceof ListUnfinishedEntity) {
                    ListUnfinishedEntity entity1 = (ListUnfinishedEntity) entity;
                    if (entity1.isStatus()) {
                        orderWorkInfos = JSON.parseArray(entity1.getMessage().toString(),OrderWorkInfo.class);
                        Log.d("aaa",orderWorkInfos.size() + "");
                    } else {
                        T.show(getContext(), entity1.getMessage().toString());
                    }
                } else {
                    T.show(getContext(), R.string.loading_data_failed);
                }
            }
        });
    }

    /**
     * 收藏技师
     *
     */
    private void collectTechnician() {
        showDialog();
        Http.getInstance().postTaskToken(NetURL.delectCollectionTechnician(orderInfo.getTechId()), "", BaseEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(getContext(), R.string.request_failed);
                    return;
                }
                if (entity instanceof BaseEntity) {
                    BaseEntity baseEntity = (BaseEntity) entity;
                    if (baseEntity.isResult()) {
                        T.show(getContext(), "收藏技师成功");
                    } else {
                        T.show(getContext(), baseEntity.getMessage());
                    }
                }

            }
        });
    }

    /**
     * 根据技师id获取技师信息
     */
    private void getTechMessage() {
        showDialog();
        Http.getInstance().getTaskToken(NetURL.getTechIdMessageV2(1), "", GetTechnicianEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity != null && entity instanceof GetTechnicianEntity) {
                    GetTechnicianEntity tech = (GetTechnicianEntity) entity;
                    if (tech.isStatus()) {
                        TechnicianMessage technicianMessage = JSON.parseObject(tech.getMessage().toString(), TechnicianMessage.class);
                        workPerson.setText(technicianMessage.getName());
                        ImageLoaderCache.getInstance().loader( NetURL.IP_PORT + technicianMessage.getAvatar(), userPhoto);
                        userName.setText(technicianMessage.getName());

                        orderCount.setText(String.valueOf(technicianMessage.getOrderCount()));
                        ratingBar.setRating(technicianMessage.getEvaluate());
                    } else {
                        T.show(getContext(), tech.getMessage().toString());
                    }
                } else {
                    T.show(getContext(), R.string.loading_data_failed);
                }
            }
        });
    }



    private void updateUI() {
        if (orderInfo == null) return;
//        orderType.setText(MyApplication.getInstance().getSkill(orderInfo.getOrderType()));
        orderNum.setText(getString(R.string.order_num, orderInfo.getOrderNum()));
        if (orderInfo.getType() == null) {
            orderType.setText("");
        } else {
            String[] types = orderInfo.getType().split(",");
            String type = "";
            for (int i = 0; i < types.length; i++) {
                type = type + getProjectName(types[i]) + ",";
            }
            if (type != "" && type.length() > 0) {
                type = type.substring(0, type.length() - 1);
            }
            orderType.setText(type);
        }
        orderTime.setText(getString(R.string.order_time1, DateCompute.getDate(orderInfo.getAgreedStartTime())));


        if ("FINISHED".equals(orderInfo.getStatus())) {//未评价
            commentStatus.setText(R.string.uncomment);
            commentStatus.setEnabled(true);
        } else if ("EXPIRED".equals(orderInfo.getStatus())){
            commentStatus.setText(R.string.timeouted);
            commentStatus.setEnabled(false);
        } else if ("COMMENTED".equals(orderInfo.getStatus())){
            commentStatus.setText(R.string.commented);
            commentStatus.setEnabled(false);
        }else if ("GIVEN_UP".equals(orderInfo.getStatus()) || "CANCELED".equals(orderInfo.getStatus())){
            commentStatus.setText(R.string.canceled_order);
            commentStatus.setEnabled(false);
        }

        if (!TextUtils.isEmpty(orderInfo.getLicense())){
            license.setText(orderInfo.getLicense());
        }else{
            license.setText("");
        }

        if (!TextUtils.isEmpty(orderInfo.getVin())){
            vin.setText(orderInfo.getVin());
        }else{
            vin.setText("");
        }

        remark.setText(orderInfo.getRemark());
        createTime.setText(DateCompute.getDate(orderInfo.getCreateTime()));
        if (orderInfo.getStartTime() != 0){
            startTime.setText(DateCompute.getDate(orderInfo.getStartTime()));
        }else {
            startTime.setText("");
        }
        if (orderInfo.getEndTime() != 0){
            endTime.setText(DateCompute.getDate(orderInfo.getEndTime()));
        }else {
            endTime.setText("");
        }
        agreeEndTime.setText(DateCompute.getDate(orderInfo.getAgreedEndTime()));
        if (!TextUtils.isEmpty(orderInfo.getTechName())){
            workPerson.setText(orderInfo.getTechName());
        }else{
            workPerson.setText("");
        }
        if (!TextUtils.isEmpty(orderInfo.getTechAvatar())){
            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + orderInfo.getTechAvatar(), userPhoto);
        }
        if (!TextUtils.isEmpty(orderInfo.getTechName())){
            userName.setText(orderInfo.getTechName());
        }else{
            userName.setText("");
        }
        if (!TextUtils.isEmpty(orderInfo.getTechPhone())){
            user_phone.setText(orderInfo.getTechPhone());
        }else{
            user_phone.setText("");
        }

        if (orderInfo.getOrderCount() != 0){
            orderCount.setText(String.valueOf(orderInfo.getOrderCount()));
        }else {
            orderCount.setText("");
        }
        if (orderInfo.getEvaluate() == null){
            ratingBar.setRating(0);
        }else {
            ratingBar.setRating(DecimalUtil.floatToInt(orderInfo.getEvaluate()));
        }

//        getTechMessage();
        updateGridView();
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

    private void updateGridView() {

        Myadapter myadapter;
        if (!TextUtils.isEmpty(orderInfo.getPhoto())){
            final String[] urlOrder;
            String urlOrderPhotos = orderInfo.getPhoto();
            if (urlOrderPhotos.contains(",")) {
                urlOrder = urlOrderPhotos.split(",");
            } else {
                urlOrder = new String[]{urlOrderPhotos};
            }
            myadapter = new Myadapter(this, urlOrder);
            order_grid.setAdapter(myadapter);

            order_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openImage(position, urlOrder);
                }
            });
        }

        if (!TextUtils.isEmpty(orderInfo.getBeforePhotos())){
            final String[] urlBefore;
            String urlBeforePhotos = orderInfo.getBeforePhotos();
            if (urlBeforePhotos.contains(",")) {
                urlBefore = urlBeforePhotos.split(",");
            } else {
                urlBefore = new String[]{urlBeforePhotos};
            }
            myadapter = new Myadapter(this, urlBefore);
            beforePhoto.setAdapter(myadapter);

            beforePhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    openImage(position, urlBefore);
                }
            });
        }


        if (!TextUtils.isEmpty(orderInfo.getAfterPhotos())){
            final String[] urlAfter;
            String urlAfterPhotos = orderInfo.getAfterPhotos();
            if (urlAfterPhotos.contains(",")) {
                urlAfter = urlAfterPhotos.split(",");
            } else {
                urlAfter = new String[]{urlAfterPhotos};
            }
            myadapter = new Myadapter(this, urlAfter);
            afterPhoto.setAdapter(myadapter);
            afterPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openImage(position, urlAfter);
                }
            });
        }

    }

    /**
     * 查看图片
     *
     * @param position
     * @param urls
     */
    private void openImage(int position, String... urls) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode && resultCode == RESULT_OK) {
            commentStatus.setText(R.string.commented);
//            commentStatus.setEnabled(false);
        }
    }
}
