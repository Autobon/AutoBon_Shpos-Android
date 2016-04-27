package cn.com.incardata.autobon_shops;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AddAccountEntity;
import cn.com.incardata.utils.T;

/**新增业务员
 * @author wanghao
 */
public class AddSalemanActivity extends BaseActivity implements View.OnClickListener{
    private EditText phone;
    private EditText name;
    private RadioGroup gender;
    private RadioButton male;
    private RadioButton female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_saleman);

        initView();
    }

    private void initView() {
        phone = (EditText) findViewById(R.id.saleman_phone);
        name = (EditText) findViewById(R.id.saleman_name);
        gender = (RadioGroup) findViewById(R.id.gender);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.submit_add_saleman).setOnClickListener(this);
        gender.check(male.getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.submit_add_saleman:
                onSubmit();
                break;
        }
    }

    private void onSubmit() {
        String phone = this.phone.getText().toString();
        String name = this.name.getText().toString();

        if (TextUtils.isEmpty(phone)){
            T.show(getContext(), R.string.phone_not_empty_tips);
            return;
        }
        if (this.phone.length() < 11){
            T.show(getContext(), R.string.error_phone_number_tips);
            return;
        }

        if (TextUtils.isEmpty(name)){
            T.show(getContext(), R.string.input_name);
            return;
        }

        NameValuePair[] params = new NameValuePair[3];
        params[0] = new BasicNameValuePair("phone", phone);
        params[1] = new BasicNameValuePair("name", name);
        params[2] = new BasicNameValuePair("gender", String.valueOf(male.isChecked()));
        showDialog(getString(R.string.submiting));
        Http.getInstance().postTaskToken(NetURL.ADD_ACCOUNT, AddAccountEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null){
                    T.show(getContext(), R.string.network_exception);
                    return;
                }
                if (entity instanceof AddAccountEntity){
                    if (((AddAccountEntity) entity).isResult()){
                        T.show(getContext(), getString(R.string.add_saleman_success));
                        Intent intent = new Intent();
                        intent.putExtra("Saleman", ((AddAccountEntity) entity).getData());
                        setResult(RESULT_OK, intent);
                        finish();
                    }else {
                        T.show(getContext(), ((AddAccountEntity) entity).getMessage());
                        return;
                    }
                }
            }
        }, params);
    }
}
