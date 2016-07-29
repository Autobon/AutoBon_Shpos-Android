package cn.com.incardata.autobon_shops;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.Construction;
import cn.com.incardata.http.response.GetTechnicianEntity;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.view.CircleImageView;
import cn.com.incardata.view.MyGridView;

/**
 * 订单详情
 */
public class OrderInfoActivity extends BaseForBroadcastActivity {
    private static final int RequestCode = 0x10;
    private TextView orderType;
    private Button commentStatus;
    private TextView orderNum;
    private TextView orderTime;
    private ImageView orderImage;
    private TextView remark;
    private TextView createTime;
    private TextView workItem;
    private TextView workPerson;
    private GridView beforePhoto;
    private GridView afterPhoto;
    private CircleImageView userPhoto;
    private TextView userName;
    private TextView orderCount;
    private RatingBar ratingBar;
    private String[] workItems;
    private OrderInfo orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        workItems = getResources().getStringArray(R.array.work_items);
        initView();
        updateUI();
    }

    private void initView() {
        orderType = (TextView) findViewById(R.id.order_type);
        commentStatus = (Button) findViewById(R.id.comment_state);
        orderNum = (TextView) findViewById(R.id.order_number);
        orderTime = (TextView) findViewById(R.id.order_time);
        orderImage = (ImageView) findViewById(R.id.order_image);
        remark = (TextView) findViewById(R.id.remark);
        createTime = (TextView) findViewById(R.id.create_time);
        workItem = (TextView) findViewById(R.id.work_item);
        workPerson = (TextView) findViewById(R.id.work_person);
        beforePhoto = (MyGridView) findViewById(R.id.work_before_grid);
        afterPhoto = (GridView) findViewById(R.id.work_after_grid);
        userPhoto = (CircleImageView) findViewById(R.id.tech_header);
        userName = (TextView) findViewById(R.id.user_name);
        orderCount = (TextView) findViewById(R.id.order_num);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        orderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (orderInfo != null){
                    bundle.putStringArray(EnlargementActivity.IMAGE_URL, new String[]{orderInfo.getPhoto()});
                }
                startActivity(EnlargementActivity.class, bundle);
            }
        });

        commentStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GoCommentActivity.class);
                intent.putExtra("UserName", orderInfo.getMainTech().getName());
                intent.putExtra("UserPhotoUrl", orderInfo.getMainTech().getAvatar());
                intent.putExtra("OrderId", orderInfo.getId());
                startActivityForResult(intent, RequestCode);//去评价
            }
        });

        orderInfo = getIntent().getParcelableExtra(AutoCon.ORDER_INFO);
        getTechInfo(orderInfo.getId());
    }

    private void getTechInfo(int orderId){
        Http.getInstance().getTaskToken(NetURL.GET_TECHNICIAN, "orderId=" + orderId, GetTechnicianEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null){
//                    T.show(getContext(), R.string.loading_data_failed);
                    return;
                }
                if (entity instanceof GetTechnicianEntity){
                    if (((GetTechnicianEntity) entity).isResult()){
                        orderCount.setText(((GetTechnicianEntity) entity).getData().getTotalOrders() + "");
                        ratingBar.setRating(((GetTechnicianEntity) entity).getData().getStarRate());
                    }else {
//                        T.show(getContext(), R.string.loading_data_failed);
                        return;
                    }
                }
            }
        });
    }

    private void updateUI(){
        if (orderInfo == null)return;
        orderType.setText(MyApplication.getInstance().getSkill(orderInfo.getOrderType()));
        orderNum.setText(getString(R.string.order_num, orderInfo.getOrderNum()));
        orderTime.setText(getString(R.string.order_time, DateCompute.getDate(orderInfo.getOrderTime())));

        if (orderInfo.getComment() == null){//未评价
            commentStatus.setText(R.string.uncomment);
        }else {
            commentStatus.setText(R.string.commented);
            commentStatus.setEnabled(false);
        }

        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + orderInfo.getPhoto(), orderImage, 0);
        remark.setText(orderInfo.getRemark());
        createTime.setText(DateCompute.getDate(orderInfo.getAddTime()));
        String works = orderInfo.getMainConstruct().getWorkItems();
        if (TextUtils.isEmpty(works)){
            workItem.setText(R.string.not);
        }else {
            if (works.contains(",")) {
                String[] items = works.split(",");
                String tempItem = "";
                for (String str : items) {
                    tempItem += workItems[Integer.parseInt(str)] + ",";
                }
                workItem.setText(tempItem.substring(0, tempItem.length() - 1));
            } else {
                workItem.setText(workItems[1]);
            }
        }
        workPerson.setText(orderInfo.getMainTech().getName());

        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + orderInfo.getMainTech().getAvatar(), userPhoto);
        userName.setText(orderInfo.getMainTech().getName());

        updateGridView(orderInfo.getMainConstruct());
    }

    private void updateGridView(Construction mainConstruct) {
        if (mainConstruct == null) return;
        Myadapter myadapter;
        final String[] urlBefore;
        String urlBeforePhotos = mainConstruct.getBeforePhotos();
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

        final String[] urlAfter;
        String urlAfterPhotos = mainConstruct.getAfterPhotos();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode && resultCode == RESULT_OK){
            commentStatus.setText(R.string.commented);
            commentStatus.setEnabled(false);
        }
    }
}
