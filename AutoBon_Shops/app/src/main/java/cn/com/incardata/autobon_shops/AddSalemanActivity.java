package cn.com.incardata.autobon_shops;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AddAccountEntity;
import cn.com.incardata.http.response.BaseEntity;
import cn.com.incardata.http.response.SalemanData;
import cn.com.incardata.utils.T;

/**新增业务员
 * @author wanghao
 */
public class AddSalemanActivity extends BaseForBroadcastActivity implements View.OnClickListener{
    private EditText phone;
    private EditText name;
    private RadioGroup gender;
    private RadioButton male;
    private RadioButton female;
    private TextView tv_setting;

    private boolean isModify = false;
    private int accountId;

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
        tv_setting = (TextView) findViewById(R.id.tv_setting);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.submit_add_saleman).setOnClickListener(this);
        gender.check(male.getId());

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) return;
        SalemanData saleman = intent.getParcelableExtra("Saleman");
        if (saleman == null) return;
        this.isModify = intent.getBooleanExtra("isModify", false);
        this.accountId = saleman.getId();//员工ID

        phone.setText(saleman.getPhone());
        name.setText(saleman.getName());
        if (saleman.isGender()) {//male
            gender.check(male.getId());
        }else {
            gender.check(female.getId());
        }
        if (isModify){
            tv_setting.setText("员工信息修改");
        }else {
            tv_setting.setText("新增账户");
        }
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
        if (isModify){
            Http.getInstance().postTaskToken(NetURL.getSalemanAccount(this.accountId), BaseEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    cancelDialog();
                    if (entity == null){
                        T.show(getContext(), R.string.operate_failed_agen);
                        return;
                    }

                    if (entity instanceof BaseEntity){
                        if (((BaseEntity) entity).isResult()){
                            T.show(getContext(), getString(R.string.modify_succeed));
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                            return;
                        }else {
                            T.show(getContext(), ((BaseEntity) entity).getMessage());
                            return;
                        }
                    }
                }
            }, params);
        }else {
            Http.getInstance().postTaskToken(NetURL.ADD_ACCOUNT, AddAccountEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    cancelDialog();
                    if (entity == null) {
                        T.show(getContext(), R.string.network_exception);
                        return;
                    }
                    if (entity instanceof AddAccountEntity) {
                        if (((AddAccountEntity) entity).isResult()) {
                            T.show(getContext(), getString(R.string.add_saleman_success));
                            Intent intent = new Intent();
                            intent.putExtra("Saleman", ((AddAccountEntity) entity).getData());
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            T.show(getContext(), ((AddAccountEntity) entity).getMessage());
                            return;
                        }
                    }
                }
            }, params);
        }
    }
}
