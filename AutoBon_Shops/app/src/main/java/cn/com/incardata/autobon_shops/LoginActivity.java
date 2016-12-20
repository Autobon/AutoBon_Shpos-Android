package cn.com.incardata.autobon_shops;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.HttpClientInCar;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.StatusCode;
import cn.com.incardata.http.response.AddAccountEntity;
import cn.com.incardata.http.response.LoginEntity;
import cn.com.incardata.http.response.Login_Data;
import cn.com.incardata.service.AService;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.SharedPre;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private Context context;
    private TextView tv_register;
    private EditText et_phone,et_pwd;
    private Button login_btn;
    private ImageView iv_eye;
    private boolean isFocus;
    private int activityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){
        context = this;
        activityId = getIntent().getIntExtra("activityId",-1);
        login_btn = (Button) findViewById(R.id.login_btn);
//        et_company = (EditText) findViewById(R.id.et_company);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        tv_register = (TextView) findViewById(R.id.tv_register);
        iv_eye = (ImageView) findViewById(R.id.iv_eye);

        tv_register.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        iv_eye.setOnClickListener(this);
        findViewById(R.id.find_pwd).setOnClickListener(this);
        String phone = "";
        String pwd = "";
        if (activityId == 1){
            phone = getIntent().getStringExtra("phone");
            pwd = getIntent().getStringExtra("pwd");
        }else if (activityId == 2){
            phone = getIntent().getStringExtra("phone");
            pwd = getIntent().getStringExtra("pwd");
        }else {
            phone = SharedPre.getString(context, AutoCon.FLAG_PHONE, "");
            pwd = SharedPre.getString(context, AutoCon.FLAG_PASSWORD, "");
        }


//        et_company.setText(SharedPre.getString(context, AutoCon.COMPANY_NAME, ""));
        et_phone.setText(phone);
        et_pwd.setText(pwd);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_eye:
                isFocus = !isFocus;
                if (isFocus) {
                    showOrHidenLoginPwd(true);
                    iv_eye.setImageResource(R.mipmap.eye_open);
                } else {
                    showOrHidenLoginPwd(false);
                    iv_eye.setImageResource(R.mipmap.eye_hidden);
                }
                break;
            case R.id.tv_register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.login_btn:
                login();
                break;
            case R.id.find_pwd:
                startActivity(FindPasswordActivity.class);
                break;
        }
    }

    /**
     * @param isShowPwd 是否显示密码
     */
    private void showOrHidenLoginPwd(boolean isShowPwd) {
        if (isShowPwd) {
            et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    private void login(){
//        String company = et_company.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String pwd = et_pwd.getText().toString().trim();

//        if(StringUtil.isEmpty(company)){
//            T.show(context,context.getString(R.string.company_empty_tips));
//            return;
//        }
        if(StringUtil.isEmpty(phone)){
            T.show(context,context.getString(R.string.empty_phone));
            return;
        }
        if(phone.length()!=11){
            T.show(context,context.getString(R.string.error_phone_number_tips));
            return;
        }
        if(StringUtil.isEmpty(pwd)){
            T.show(context,context.getString(R.string.empty_password));
            return;
        }
//        startActivity(MainActivity.class);//审核通过
//        return;

        if(NetWorkHelper.isNetworkAvailable(context)){
            MyAsyncTask myAsyncTask = new MyAsyncTask();
            showDialog(getString(R.string.login_ing));
            myAsyncTask.execute(phone,pwd);  //传递参数到AsyncTask类中
        }else{
            T.show(context,context.getString(R.string.network_exception));
            return;
        }
    }

    class MyAsyncTask extends AsyncTask<String,Void,String>{
        private String shortName;
        private String phone;
        private String password;

        @Override
        protected String doInBackground(String... params) {

            this.phone = params[0];
            this.password = params[1];

            BasicNameValuePair bv_phone = new BasicNameValuePair("phone",params[0]);
            BasicNameValuePair bv_password = new BasicNameValuePair("password",params[1]);
            try{
                String result = HttpClientInCar.postLoginHttpToken(context, NetURL.LOGIN_URLV2,bv_phone,bv_password);
                return result;
            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            cancelDialog();
            if (StringUtil.isEmpty(result)) {
                T.show(context,context.getString(R.string.request_failed));
                return;
            }
            LoginEntity loginEntity = JSON.parseObject(result,LoginEntity.class);
            if(loginEntity == null){
                T.show(context,context.getString(R.string.operate_failed_agen));
                return;
            }
            if(loginEntity.isStatus()){  //成功
//                SharedPre.setSharedPreferences(context,AutoCon.COMPANY_NAME,this.shortName);
                SharedPre.setSharedPreferences(context,AutoCon.FLAG_PHONE,this.phone);
                SharedPre.setSharedPreferences(context, AutoCon.FLAG_PASSWORD,this.password);  //保存信息
                Login_Data login_data = JSON.parseObject(loginEntity.getMessage().toString(),Login_Data.class);
                //TODO 跳转主页
                if (login_data.getCooperator() == null){
                    startActivity(CooperativeOneActivity.class);//未提交资料
                }else if(login_data.getCooperator().getStatusCode() == StatusCode.COOPERATIVE_REVIEW_SUCCESSFUL){
                    MyApplication.getInstance().setUser(login_data.getCooperator());
                    startActivity(MainActivity.class);//审核通过
//                    startActivity(CooperativeOneActivity.class);
//                    startActivity(MyOrderActivity.class);
                }else {
                    startActivity(MainReviewActivity.class);
                }
                startService(new Intent(getContext(), AService.class));
            }else{  //失败
                T.show(context,loginEntity.getMessage().toString());
                return;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
