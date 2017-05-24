package cn.com.incardata.autobon_shops;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AppointTechEntity;
import cn.com.incardata.http.response.CollectionTechnician;
import cn.com.incardata.http.response.CollectionTechnician_Data;
import cn.com.incardata.http.response.TechnicianMessage;
import cn.com.incardata.utils.DecimalUtil;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.CircleImageView;

/**
 * 收藏技师详情界面
 * Created by yang on 2017/5/24.
 */
public class CollectionTechnicianInfoActivity extends BaseActivity {

    private ImageView back;
    private CollectionTechnician_Data collectionTechnician_data;
    private CollectionTechnician technicianMessage;
    private TextView name, tv_phone, tv_distance;
    private CircleImageView circleImageView;
    private TextView ge_workYear, yin_workYear, che_workYear, mei_workYear;
    private RatingBar ratingbar;
    private TextView orderCount;
    private RatingBar[] ratingBars = new RatingBar[4];
    private int orderId;
    private Button submit;
    private int activityId;
    private ImageView img_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_technician_info);
        initView();
    }

    public void initView() {
        collectionTechnician_data = getIntent().getParcelableExtra("techMessage");
        if (collectionTechnician_data != null){
            technicianMessage = collectionTechnician_data.getTechnician();
        }
        orderId = getIntent().getIntExtra("orderId", -1);
        activityId = getIntent().getIntExtra("activityId", -1);
        back = (ImageView) findViewById(R.id.iv_back);
        name = (TextView) findViewById(R.id.name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        orderCount = (TextView) findViewById(R.id.orderCount);
        img_distance = (ImageView) findViewById(R.id.img_distance);
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        ge_workYear = (TextView) findViewById(R.id.ge_workYear);
        yin_workYear = (TextView) findViewById(R.id.yin_workYear);
        che_workYear = (TextView) findViewById(R.id.che_workYear);
        mei_workYear = (TextView) findViewById(R.id.mei_workYear);
        circleImageView = (CircleImageView) findViewById(R.id.header_image);
        ratingBars[0] = (RatingBar) findViewById(R.id.ge_ratingBar);
        ratingBars[1] = (RatingBar) findViewById(R.id.yin_ratingBar);
        ratingBars[2] = (RatingBar) findViewById(R.id.che_ratingBar);
        ratingBars[3] = (RatingBar) findViewById(R.id.mei_ratingBar);
        submit = (Button) findViewById(R.id.submit);

        if (technicianMessage == null || orderId == -1 || activityId == -1) {
            T.show(this, "数据加载失败");
            return;
        } else {
            if (activityId == 4) {
                submit.setPadding(10, 5, 10, 5);
                submit.setBackgroundDrawable(getResources().getDrawable(R.mipmap.phone));
                submit.setText("");
                tv_distance.setVisibility(View.GONE);
                img_distance.setVisibility(View.GONE);
            }
            if (technicianMessage.getAvatar() != null) {
                String imageUrl = technicianMessage.getAvatar();
                Log.i("test", "imageUrl=======>" + imageUrl);
                ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + imageUrl, circleImageView);
            }
            name.setText(technicianMessage.getName());
            tv_phone.setText(technicianMessage.getPhone());
            tv_distance.setText("0.00km");
            orderCount.setText(String.valueOf(0));
            ratingbar.setRating(0);
            ge_workYear.setText(technicianMessage.getFilmWorkingSeniority() + "年");
            yin_workYear.setText(technicianMessage.getCarCoverWorkingSeniority() + "年");
            che_workYear.setText(technicianMessage.getColorModifyWorkingSeniority() + "年");
            mei_workYear.setText(technicianMessage.getBeautyWorkingSeniority() + "年");
            ratingBars[0].setRating(technicianMessage.getFilmLevel());
            ratingBars[1].setRating(technicianMessage.getCarCoverLevel());
            ratingBars[2].setRating(technicianMessage.getColorModifyLevel());
            ratingBars[3].setRating(technicianMessage.getBeautyLevel());
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityId == 4) {
                    Uri uri = Uri.parse("tel:" + technicianMessage.getPhone());
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(intent);
                } else {
                    addTechnician();
                }
            }
        });
    }

    /**
     * 指派技师
     */
    public void addTechnician() {
        showDialog(null);
        Http.getInstance().postTaskToken(NetURL.APPOINTV2, AppointTechEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(CollectionTechnicianInfoActivity.this, R.string.operate_failed_agen);
                    return;
                }
                AppointTechEntity tech = (AppointTechEntity) entity;
                if (tech.isStatus()) {
                    Log.i("test", "指定技师成功");
                    T.show(CollectionTechnicianInfoActivity.this, R.string.appoint_technician_succeed);
                    AddContactActivity.isFinish = true;
                    if (activityId == 1) {
                        finish();
                    } else if (activityId == 2) {
                        finish();
                    } else {
                        finish();
                    }
                } else {
                    T.show(CollectionTechnicianInfoActivity.this, tech.getMessage());
                }
            }
        }, new BasicNameValuePair("orderId", String.valueOf(orderId)), new BasicNameValuePair("techId", String.valueOf(technicianMessage.getId())));
    }
}
