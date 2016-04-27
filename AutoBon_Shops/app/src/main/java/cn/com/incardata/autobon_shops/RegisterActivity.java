package cn.com.incardata.autobon_shops;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.RegisterEntity;
import cn.com.incardata.http.response.VerifySmsEntity;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;

/**
 * Created by zhangming on 2016/3/28.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private Context context;
    private EditText et_phone,et_company,et_code,et_pwd,et_confirm_pwd;
    private Button btn_send_code,btn_register;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init(){
        context = this;
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_company = (EditText) findViewById(R.id.et_company);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_confirm_pwd = (EditText) findViewById(R.id.et_confirm_pwd);
        btn_send_code = (Button) findViewById(R.id.btn_send_code);
        btn_register = (Button) findViewById(R.id.btn_register);

        btn_send_code.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        findViewById(R.id.tv_protocal).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send_code:
                sendValidCode();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_protocal:
                startActivity(ServiceProtocalActivity.class);
                break;
        }
    }

    private void sendValidCode(){
        String phone = et_phone.getText().toString().trim();
        if(StringUtil.isEmpty(phone)){
            T.show(this,getString(R.string.phone_not_empty_tips));
            return;
        }
        if(phone.length()!=11){
            T.show(this,getString(R.string.error_phone_number_tips));
            return;
        }

        if(NetWorkHelper.isNetworkAvailable(context)) {
            BasicNameValuePair bv_phone = new BasicNameValuePair("phone",phone);
            Http.getInstance().getTaskToken(NetURL.VERIFY_SMS, VerifySmsEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null) {
                        T.show(context, context.getString(R.string.request_failed));
                        return;
                    }
                    VerifySmsEntity verifySmsEntity = (VerifySmsEntity) entity;
                    if(verifySmsEntity.isResult()){
                        countDownTimer(60); //验证码发送成功后,再倒计时60秒
                        T.show(context,context.getString(R.string.send_code_success));
                        return;
                    }
                }
            },bv_phone);
        }else{
            T.show(context,getString(R.string.network_exception));
            return;
        }
    }

    /**
     * 倒计时
     * @param time
     */
    private void countDownTimer(int time){
        new CountDownTimer(time*1000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                btn_send_code.setText("("+String.valueOf(millisUntilFinished/1000)+")"+context.getString(R.string.time_tips));
                btn_send_code.setClickable(false);
            }
            @Override
            public void onFinish() {
                btn_send_code.setText(context.getString(R.string.btn_text_send_code_repeat));
                btn_send_code.setClickable(true);
            }
        }.start();
    }

    private void register(){
        String shortName = et_company.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String code = et_code.getText().toString().trim();
        String one_pwd = et_pwd.getText().toString().trim();
        String two_pwd = et_confirm_pwd.getText().toString().trim();

        if(StringUtil.isEmpty(shortName)){
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
        if(StringUtil.isEmpty(code)){
            T.show(context,context.getString(R.string.empty_code));
            return;
        }
        if(code.length()!=6){  //验证码的长度不为6位,提示用户
            T.show(context,context.getResources().getString(R.string.code_length_tips));
            return;
        }
        if(StringUtil.isEmpty(one_pwd)){
            T.show(context,context.getString(R.string.empty_password));
            return;
        }
        if(!one_pwd.equals(two_pwd)){
            T.show(context,context.getString(R.string.different_pwd_tips));
            return;
        }
        if(one_pwd.length()<8){
            T.show(context,context.getString(R.string.password_length_tips));
            return;
        }
        if(!one_pwd.matches(".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]")){  //密码长度至少为8位,且为数字或字母组合
            T.show(context,context.getString(R.string.error_password));
            return;
        }

        List<BasicNameValuePair> mList = new ArrayList<BasicNameValuePair>();
        mList.add(new BasicNameValuePair("shortname",shortName));
        mList.add(new BasicNameValuePair("phone",phone));
        mList.add(new BasicNameValuePair("password",one_pwd));
        mList.add(new BasicNameValuePair("verifySms",code));

        if(NetWorkHelper.isNetworkAvailable(context)) {
            Http.getInstance().postTaskToken(NetURL.REGISTER_URL, RegisterEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null) {
                        T.show(context, context.getString(R.string.operate_failed_agen));
                        return;
                    }
                    RegisterEntity registerEntity = (RegisterEntity) entity;
                    if(registerEntity.isResult()){  //成功
                        T.show(context,context.getString(R.string.register_success_tips));
                        // 注册成功后清空任务栈返回登录界面
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{  //失败
                        T.show(context,registerEntity.getMessage());
                        return;
                    }
                }
            },(BasicNameValuePair[]) mList.toArray(new BasicNameValuePair[mList.size()]));
        }else{
            T.show(context,getString(R.string.network_exception));
            return;
        }
    }

}
