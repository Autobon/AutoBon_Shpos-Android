package cn.com.incardata.autobon_shops;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AppointTechEntity;
import cn.com.incardata.utils.T;

/**
 * 新建套餐界面
 * <p>Created by wangyang on 2019/10/9.</p>
 */
public class AddSetMealActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed_set_meal_name;                     //套餐名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_set_meal);
        initView();
    }


    /**
     * 初始化
     */
    private void initView() {
        ed_set_meal_name = (EditText) findViewById(R.id.ed_set_meal_name);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_add:
                addSetMeal();
                break;
        }
    }


    /**
     *
     * 添加套餐
     */
    private void addSetMeal(){

        String setMealName = ed_set_meal_name.getText().toString().trim();
        if (TextUtils.isEmpty(setMealName)){
            T.show(this,"请输入套餐名称");
            return;
        }
        showDialog();
        Http.getInstance().postTaskToken(NetURL.ADDSETMEAL, AppointTechEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if(entity == null){
                    T.show(getContext(),R.string.operate_failed_agen);
                    return;
                }
                AppointTechEntity response = (AppointTechEntity) entity;
                if(response.isStatus()){
                    Log.i("test","新建套餐成功");
                    T.show(getContext(), getString(R.string.add_set_meal_success));
                    MySetMealActivity.isRefreshed = true;
                    ProductDetailedActivity.isGetSetMeal = true;
                    finish();
                }else{
                    T.show(getContext(),response.getMessage());
                }
            }
        }, new BasicNameValuePair("name",setMealName), new BasicNameValuePair("offerIds", ""));


    }


}
