package cn.com.incardata.autobon_shops;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AppointTechEntity;
import cn.com.incardata.http.response.TechnicianMessage;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.DecimalUtil;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.CircleImageView;

/**
 * 技师详情界面
 * Created by yang on 2016/11/21.
 */
public class TechnicianInfoActivity extends BaseActivity {
    private ImageView back;
    private TechnicianMessage technicianMessage;
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
        setContentView(R.layout.activity_technician_info);
        initView();
    }


    public void initView() {
        technicianMessage = getIntent().getParcelableExtra("techMessage");
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
        } else {
            if (activityId == 4) {
                submit.setPadding(10,5,10,5);
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
            if (technicianMessage.getDistance() == null || technicianMessage.getDistance() == 0) {
                tv_distance.setText("0.00km");
            } else {
                tv_distance.setText(DecimalUtil.FloatDecimal2(technicianMessage.getDistance()) + "km");
            }
            if (technicianMessage.getOrderCount() == null){
                orderCount.setText(String.valueOf(0));
            }else {
                orderCount.setText(String.valueOf(technicianMessage.getOrderCount()));
            }
            if (technicianMessage.getEvaluate() == null){
                ratingbar.setRating(0);
            }else {
                ratingbar.setRating(DecimalUtil.floatToInt(technicianMessage.getEvaluate()));
            }
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
                }else {
                    addTechnician();
                }
            }
        });
    }

    public void addTechnician() {
        showDialog(null);
        Http.getInstance().postTaskToken(NetURL.APPOINTV2, AppointTechEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(TechnicianInfoActivity.this, R.string.operate_failed_agen);
                    return;
                }
                AppointTechEntity tech = (AppointTechEntity) entity;
                if (tech.isStatus()) {
                    Log.i("test", "指定技师成功");
                    T.show(TechnicianInfoActivity.this, R.string.appoint_technician_succeed);
                    if (activityId == 1) {
                        Intent i = new Intent(TechnicianInfoActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else if (activityId == 2) {
                        Intent i = new Intent(TechnicianInfoActivity.this, UnfinishOrderActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(TechnicianInfoActivity.this, UnFinishOrderInfoActivity.class);
                        startActivity(i);
                        finish();
                    }


                } else {
                    T.show(TechnicianInfoActivity.this, tech.getMessage());
                }
            }
        }, new BasicNameValuePair("orderId", String.valueOf(orderId)), new BasicNameValuePair("techId", String.valueOf(technicianMessage.getId())));
    }


}
