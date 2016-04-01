package cn.com.incardata.autobon_shops;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.CommonSpinnerAdapter;

/**
 * Created by zhangming on 2016/3/31.
 */
public class CooperativeThreeActivity extends BaseActivity implements View.OnClickListener{
    private Spinner provinceSpinner,citySpinner,areaSpinner;
    private CommonSpinnerAdapter provinceAdapter,cityAdapter,areaAdapter;
    private EditText et_invoice_header,et_tax_no,et_mail_code,et_full_address,et_contact,et_contactPhone;
    private Button join_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cooperative_info_three_activity);
        init();
    }

    private void init(){
        provinceSpinner = (Spinner) findViewById(R.id.provinceSpinner);
        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        areaSpinner = (Spinner) findViewById(R.id.areaSpinner);

        et_invoice_header = (EditText) findViewById(R.id.et_invoice_header);
        et_tax_no = (EditText) findViewById(R.id.et_tax_no);
        et_mail_code = (EditText) findViewById(R.id.et_mail_code);
        et_full_address = (EditText) findViewById(R.id.et_full_address);
        et_contact = (EditText) findViewById(R.id.et_contact);
        et_contactPhone = (EditText) findViewById(R.id.et_contactPhone);
        join_btn = (Button) findViewById(R.id.join_btn);

        List<String> provinceList = new ArrayList<String>();
        List<String> cityList = new ArrayList<String>();
        List<String> areaList = new ArrayList<String>();

        provinceList.add("AA");
        provinceList.add("BB");
        provinceList.add("CC");
        cityList.add("MM");
        cityList.add("NN");
        cityList.add("KK");
        areaList.add("XX");
        areaList.add("YY");
        areaList.add("ZZ");

        provinceAdapter = new CommonSpinnerAdapter(this,provinceList);
        cityAdapter = new CommonSpinnerAdapter(this,cityList);
        areaAdapter = new CommonSpinnerAdapter(this,areaList);

        provinceSpinner.setAdapter(provinceAdapter);
        citySpinner.setAdapter(cityAdapter);
        areaSpinner.setAdapter(areaAdapter);

        join_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.join_btn:
                submitInfo();
                break;
        }
    }

    /**
     * 加盟
     */
    private void submitInfo(){
        String invoiceHeader = et_invoice_header.getText().toString().trim(); //发票抬头
        String taxIdNo = et_tax_no.getText().toString().trim(); //纳税识别号
        String postcode = et_mail_code.getText().toString().trim(); //邮政编码

        String province = (String)provinceSpinner.getSelectedItem();
        String city = (String)citySpinner.getSelectedItem();
        String area = (String)areaSpinner.getSelectedItem();

        String address = et_full_address.getText().toString().trim();  //详细地址
        String contact = et_contact.getText().toString().trim();  //联系人姓名
        String contactPhone = et_contactPhone.getText().toString().trim(); //联系人电话

        Bundle bundle = getIntent().getExtras();
        String fullname = bundle.getString("company");
        String businessLicense = bundle.getString("company_number");
        String corporationName = bundle.getString("name");
        String corporationIdNo = bundle.getString("card_number");
        String bussinessLicensePic = bundle.getString("business_pic_url");
        String corporationIdPicA = bundle.getString("corporation_pic_url");
        String longitude = bundle.getString("longitude");
        String latitude = bundle.getString("latitude");

        //TODO submit
    }
}
