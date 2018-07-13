package cn.com.incardata.autobon_shops;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.incardata.http.response.SerializableMap;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.SelectPopupView;
import cn.com.incardata.view.selftimeview.TimePopupWindow;

/**
 * 订单查询界面
 * <p>Created by wangyang on 2018/6/14.</p>
 */
public class OrderQueryActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_status;                             //状态
    private TextView tv_work_time;                          //施工日期
    private EditText ed_carframe_number;                    //车架号
    private EditText ed_order_phone;                        //下单人手机号

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_query);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_work_time = (TextView) findViewById(R.id.tv_work_time);
        ed_carframe_number = (EditText) findViewById(R.id.ed_carframe_number);
        ed_order_phone = (EditText) findViewById(R.id.ed_order_phone);

        tv_status.setTag(-1);

        findViewById(R.id.iv_back).setOnClickListener(this);
        tv_status.setOnClickListener(this);
        tv_work_time.setOnClickListener(this);
        findViewById(R.id.btn_query).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_status:
                showPopupWindow((Integer) tv_status.getTag());
                break;
            case R.id.tv_work_time:
                InputMethodManager manager = (InputMethodManager) getSystemService(getContext().INPUT_METHOD_SERVICE);
                boolean isOpen = manager.isActive();
                if (isOpen) {  //软键盘处于开状态
                    manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);  //强制隐藏软键盘
                }
                showTimePop(tv_work_time);
                break;
            case R.id.btn_query:
                query();
                break;
        }
    }

    List<String> statusStr = new ArrayList<>();
    SelectPopupView pop;

    /**
     * 状态选择弹出框
     *
     * @param checkId
     */
    public void showPopupWindow(int checkId) {
        if (pop == null) {
            statusStr.add("未接单");
            statusStr.add("施工中");
            statusStr.add(getString(R.string.uncomment1));
            statusStr.add(getString(R.string.commented));
            pop = new SelectPopupView(this);
            pop.init();
            pop.setListener(listener);
        }
        pop.setData(statusStr, getString(R.string.please_select_status), checkId);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    public SelectPopupView.OnCheckedListener listener = new SelectPopupView.OnCheckedListener() {
        @Override
        public void onChecked(int index) {
            tv_status.setTag(index);
            tv_status.setText(statusStr.get(index));
        }
    };

    TimePopupWindow popupWindow;

    /**
     * 施工时间弹出框
     *
     * @param textView
     */
    private void showTimePop(TextView textView) {
        if (popupWindow == null || !popupWindow.isShowing()) {
            //时间选择器
            popupWindow = new TimePopupWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY, textView);
            //时间选择后回调
            popupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date) {
                    tv_work_time.setText(DateCompute.getYearMonthDayDate(date.getTime()));
                }
            });
            popupWindow.showAtLocation(tv_work_time, Gravity.BOTTOM, 0, 0);
        }
    }

    private void query(){
        Map<String,String> param = new HashMap<>();

        String statusStr = tv_status.getText().toString().trim();
        if (!TextUtils.isEmpty(statusStr) && (int)tv_status.getTag() != -1){
            param.put("status",String.valueOf((int)tv_status.getTag() + 1));
        }else {
            param.put("status","5");
        }

        String workTime = tv_work_time.getText().toString().trim();
        if (!TextUtils.isEmpty(workTime)){
            param.put("workDate",workTime);
        }
        String VinStr = ed_carframe_number.getText().toString().trim();
        if (!TextUtils.isEmpty(VinStr)){
            param.put("vin",workTime);
        }
        String phoneStr = ed_order_phone.getText().toString().trim();
        if (!TextUtils.isEmpty(phoneStr)){
            param.put("phone",phoneStr);
        }

        Intent intent = new Intent();
        SerializableMap map = new SerializableMap();
        map.setMap(param);
        Bundle bundle = new Bundle();
        bundle.putSerializable("map",map);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }
}
