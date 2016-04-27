package cn.com.incardata.autobon_shops;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.HttpClientInCar;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.StatusCode;
import cn.com.incardata.http.response.LoginEntity;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.SharedPre;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private Context context;
    private TextView tv_register;
    private EditText et_company,et_phone,et_pwd;
    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){
        context = this;
        login_btn = (Button) findViewById(R.id.login_btn);
        et_company = (EditText) findViewById(R.id.et_company);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        tv_register = (TextView) findViewById(R.id.tv_register);

        tv_register.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        findViewById(R.id.find_pwd).setOnClickListener(this);

        et_company.setText(SharedPre.getString(context, AutoCon.COMPANY_NAME, ""));
        et_phone.setText(SharedPre.getString(context, AutoCon.FLAG_PHONE, ""));
        et_pwd.setText(SharedPre.getString(context, AutoCon.FLAG_PASSWORD, ""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.login_btn:
//                startActivity(ShareActivity.class);
                login();
                break;
            case R.id.find_pwd:
                startActivity(FindPasswordActivity.class);
                break;
        }
    }

    private void login(){
        String company = et_company.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String pwd = et_pwd.getText().toString().trim();

        if(StringUtil.isEmpty(company)){
            T.show(context,context.getString(R.string.company_empty_tips));
            return;
        }
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

        if(NetWorkHelper.isNetworkAvailable(context)){
            MyAsyncTask myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute(company,phone,pwd);  //传递参数到AsyncTask类中
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
            this.shortName = params[0];
            this.phone = params[1];
            this.password = params[2];

            BasicNameValuePair bv_shortname = new BasicNameValuePair("shortname",params[0]);
            BasicNameValuePair bv_phone = new BasicNameValuePair("phone",params[1]);
            BasicNameValuePair bv_password = new BasicNameValuePair("password",params[2]);
            try{
                String result = HttpClientInCar.postLoginHttpToken(context, NetURL.LOGIN_URL,bv_shortname,bv_phone,bv_password);
                return result;
            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (StringUtil.isEmpty(result)) {
                T.show(context,context.getString(R.string.request_failed));
                return;
            }
            LoginEntity loginEntity = JSON.parseObject(result,LoginEntity.class);
            if(loginEntity == null){
                T.show(context,context.getString(R.string.operate_failed_agen));
                return;
            }
            if(loginEntity.isResult()){  //成功
                SharedPre.setSharedPreferences(context,AutoCon.COMPANY_NAME,this.shortName);
                SharedPre.setSharedPreferences(context,AutoCon.FLAG_PHONE,this.phone);
                SharedPre.setSharedPreferences(context, AutoCon.FLAG_PASSWORD,this.password);  //保存信息
                //TODO 跳转主页
                if (loginEntity.getData().getCooperator() == null){
                    startActivity(CooperativeOneActivity.class);//未提交资料
                }else if(loginEntity.getData().getCooperator().getStatusCode() == StatusCode.COOPERATIVE_REVIEW_SUCCESSFUL){
                    MyApplication.getInstance().setUser(loginEntity.getData().getCooperator());
                    startActivity(MainActivity.class);//审核通过
                }else {
                    startActivity(MainReviewActivity.class);
                }
                finish();
            }else{  //失败
                T.show(context,loginEntity.getMessage());
                return;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
