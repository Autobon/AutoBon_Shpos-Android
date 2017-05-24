package cn.com.incardata.autobon_shops;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.OrderCountEntity;
import cn.com.incardata.utils.SpannableStringUtil;

/**
 * 商户中心
 */
public class PersonalActivity extends BaseForBroadcastActivity implements View.OnClickListener {
    private TextView companyName;
    private TextView orderCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        initView();
        getOrderCount();
    }

    private void initView() {
        companyName = (TextView) findViewById(R.id.company_name);
        orderCount = (TextView) findViewById(R.id.order_count);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.order_layout).setOnClickListener(this);
        findViewById(R.id.collection_layout).setOnClickListener(this);
        findViewById(R.id.cooperater_layout).setOnClickListener(this);
        findViewById(R.id.salesman_layout).setOnClickListener(this);
        findViewById(R.id.notify_layout).setOnClickListener(this);
        findViewById(R.id.modify_password_layout).setOnClickListener(this);
        findViewById(R.id.phone_number).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);

        String str = getString(R.string.order_count, 0);
        orderCount.setText(SpannableStringUtil.getForegroundColorSpan(getContext(), str, 3, str.length(), getResources().getColor(R.color.main_orange)));
        if (MyApplication.getUser() == null) return;
        companyName.setText(MyApplication.getInstance().getUser().getFullname());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.order_layout:
                startActivity(MyOrderActivity.class);
                break;
            case R.id.collection_layout:
                startActivity(CollectionContactActivity.class);
                break;
            case R.id.cooperater_layout:
                startActivity(ReviewMessageActivity.class);
                break;
            case R.id.salesman_layout:
                startActivity(SalesmanManageActivity.class);
                break;
            case R.id.notify_layout:
                startActivity(NotificationMessageActivity.class);
                break;
            case R.id.modify_password_layout:
                startActivity(ModifyPasswordActivity.class);
                break;
            case R.id.phone_number:
                callPhone(getString(R.string.phone_4001871500));
                break;
            case R.id.logout:
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void getOrderCount() {
        Http.getInstance().postTaskToken(NetURL.ORDER_COUNT, "", OrderCountEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity != null && entity instanceof OrderCountEntity){
                    String str = getString(R.string.order_count, ((OrderCountEntity) entity).isResult() ? ((OrderCountEntity) entity).getData() : 0);
                    orderCount.setText(SpannableStringUtil.getForegroundColorSpan(getContext(), str, 3, str.length(), getResources().getColor(R.color.main_orange)));
                    if (MyApplication.getUser() != null){
                        companyName.setText(MyApplication.getUser().getFullname());
                    }
                }
            }
        });
    }

    private void callPhone(final String phone){
        new AlertDialog.Builder(this, R.style.TipsDialog)
                .setMessage(phone)
                .setPositiveButton(R.string.call, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phone));
                        startActivity(intent);
                    }
                }).setNegativeButton(R.string.cancel_call, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();

    }
}
