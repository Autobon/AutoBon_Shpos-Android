package cn.com.incardata.autobon_shops;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.CommentEntity;
import cn.com.incardata.http.response.GetTechnicianEntity;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.OrderInfoEntity;
import cn.com.incardata.http.response.TechnicianMessage;
import cn.com.incardata.utils.DecimalUtil;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.CircleImageView;

/**评价
 * @author wanghao
 */
public class GoCommentActivity extends BaseActivity implements View.OnClickListener{
    private TextView userName;
    private CircleImageView userHead;
    private TextView orderCount;
    private RatingBar mRatingBar;

    private RatingBar comRatingBar;
    private CheckBox[] comItem = new CheckBox[6];
    private EditText otherCom;

    private String userName_extra;
    private String userphotoUrl;
    private int OrderId;
    private int OrderCount;
    private Float evaluate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_comment);
        initView();
//        getTechInfo();
//        getTechMessage();
    }

    private void initView() {
        userName = (TextView) findViewById(R.id.user_name);
        userHead = (CircleImageView) findViewById(R.id.tech_header);
        orderCount = (TextView) findViewById(R.id.order_num);
        mRatingBar = (RatingBar) findViewById(R.id.ratingbar);
        comRatingBar = (RatingBar) findViewById(R.id.comment_ratingbar);
        comItem[0] = (CheckBox) findViewById(R.id.com_1);
        comItem[1] = (CheckBox) findViewById(R.id.com_2);
        comItem[2] = (CheckBox) findViewById(R.id.com_3);
        comItem[3] = (CheckBox) findViewById(R.id.com_4);
        comItem[4] = (CheckBox) findViewById(R.id.com_5);
        comItem[5] = (CheckBox) findViewById(R.id.com_6);
        otherCom = (EditText) findViewById(R.id.edit_comment);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.submit_comment).setOnClickListener(this);

        userName_extra = getIntent().getStringExtra("UserName");
        userphotoUrl = getIntent().getStringExtra("UserPhotoUrl");
        OrderId = getIntent().getIntExtra("OrderId", -1);
        OrderCount = getIntent().getIntExtra("orderCount",-1);
        evaluate = getIntent().getFloatExtra("evaluate",0);

        if (TextUtils.isEmpty(userName_extra) || OrderId == -1 || OrderCount == -1){
            T.show(getContext(),R.string.loading_data_failed);
        }else {
            userName.setText(userName_extra);
            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + userphotoUrl, userHead);
            orderCount.setText(String.valueOf(OrderCount));
            if (evaluate == null){
                mRatingBar.setRating(0);
            }else {
                mRatingBar.setRating(DecimalUtil.floatToInt(evaluate));
            }
        }
    }

//    /**
//     * 显示技师信息对话框
//     */
//    private void getTechMessage() {
//        showDialog();
//        Http.getInstance().getTaskToken(NetURL.getTechMessageV2(OrderId), "" , GetTechnicianEntity.class, new OnResult() {
//            @Override
//            public void onResult(Object entity) {
//                cancelDialog();
//                if (entity != null && entity instanceof GetTechnicianEntity){
//                    GetTechnicianEntity tech = (GetTechnicianEntity) entity;
//                    if (tech.isStatus()){
//                        TechnicianMessage technicianMessage = JSON.parseObject(tech.getMessage().toString(),TechnicianMessage.class);
//                        orderCount.setText(String.valueOf(technicianMessage.getOrderCount()));
//                        mRatingBar.setRating(5 * technicianMessage.getEvaluate());
////                        userName.setText(technicianMessage.getName());
//                        if (TextUtils.isEmpty(userName_extra)){
//                            userName.setText(technicianMessage.getName());
//                        }
//                        if (TextUtils.isEmpty(userphotoUrl)){
//                            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + userphotoUrl, userHead);
//                        }
//                    }else {
//                        T.show(getContext(),tech.getMessage().toString());
//                    }
//                }else {
//                    T.show(getContext(), R.string.loading_data_failed);
//                }
//            }
//        });
//    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.submit_comment:
                onComment();
                break;
        }
    }

    private float star;
    private void onComment() {
        star = comRatingBar.getRating();
        List<NameValuePair> params = new ArrayList<NameValuePair>(9);
        params.add(new BasicNameValuePair("orderId", String.valueOf(OrderId)));
        params.add(new BasicNameValuePair("star", String.valueOf((int)star)));
        params.add(new BasicNameValuePair("arriveOnTime", String.valueOf(comItem[0].isChecked())));
        params.add(new BasicNameValuePair("completeOnTime", String.valueOf(comItem[1].isChecked())));
        params.add(new BasicNameValuePair("professional", String.valueOf(comItem[2].isChecked())));
        params.add(new BasicNameValuePair("dressNeatly", String.valueOf(comItem[3].isChecked())));
        params.add(new BasicNameValuePair("carProtect", String.valueOf(comItem[4].isChecked())));
        params.add(new BasicNameValuePair("goodAttitude", String.valueOf(comItem[5].isChecked())));
        params.add(new BasicNameValuePair("advice", otherCom.getText().toString()));

        showDialog(getString(R.string.submiting_comment));
        Http.getInstance().postTaskToken(NetURL.COMMENTV2, CommentEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null){
                    T.show(GoCommentActivity.this, R.string.submit_comment_failed_please_again);
                    return;
                }
                if (entity instanceof CommentEntity){
                    if (((CommentEntity) entity).isStatus()){
                        setResult(RESULT_OK);
                        //分享
                        Bundle bundle = new Bundle();
                        bundle.putString("UserName", userName_extra);
                        bundle.putString("UserPhotoUrl", userphotoUrl);
                        bundle.putString("OrderCount", orderCount.getText().toString());
                        bundle.putFloat("techStar", mRatingBar.getRating());
                        bundle.putFloat("Star", comRatingBar.getRating());
                        bundle.putInt("orderId",OrderId);
                        startActivity(ShareActivity.class, bundle);
                        finish();
                    }else {
                        T.show(GoCommentActivity.this, ((CommentEntity) entity).getMessage());
                        return;
                    }
                }
            }
        }, params.toArray(new NameValuePair[9]));
    }
}
