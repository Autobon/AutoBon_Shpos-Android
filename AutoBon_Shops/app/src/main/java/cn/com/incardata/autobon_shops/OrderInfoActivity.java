package cn.com.incardata.autobon_shops;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.GetTechnicianEntity;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.view.CircleImageView;

/**
 * 订单详情
 */
public class OrderInfoActivity extends BaseActivity {
    private static final int RequestCode = 0x10;
    private TextView orderType;
    private Button commentStatus;
    private TextView orderNum;
    private TextView orderTime;
    private ImageView orderImage;
    private TextView remark;
    private TextView createTime;
    private CircleImageView userPhoto;
    private TextView userName;
    private TextView orderCount;
    private RatingBar ratingBar;


    private OrderInfo orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

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

        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + orderInfo.getMainTech().getAvatar(), userPhoto);
        userName.setText(orderInfo.getMainTech().getName());
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
