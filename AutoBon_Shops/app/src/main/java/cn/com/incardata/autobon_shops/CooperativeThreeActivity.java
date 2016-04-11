package cn.com.incardata.autobon_shops;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.CommonSpinnerAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.CooperativeSubmitEntity;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.CheckTipsDialog;

/**
 * Created by zhangming on 2016/3/31.
 */
public class CooperativeThreeActivity extends BaseActivity implements View.OnClickListener{
    private Context context;
    private Spinner provinceSpinner,citySpinner,areaSpinner;
    private CommonSpinnerAdapter provinceAdapter,cityAdapter,areaAdapter;
    private EditText et_invoice_header,et_tax_no,et_mail_code,et_full_address,et_contact,et_contactPhone;
    private Button join_btn;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cooperative_info_three_activity);
        init();
    }

    private void init(){
        context = this;
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
        iv_back = (ImageView) findViewById(R.id.iv_back);

        List<String> provinceList = new ArrayList<String>();
        List<String> cityList = new ArrayList<String>();
        List<String> areaList = new ArrayList<String>();

        provinceList.add(getString(R.string.choose_province));
        provinceList.add("AA");
        provinceList.add("BB");
        provinceList.add("CC");

        cityList.add(getString(R.string.choose_city));
        cityList.add("MM");
        cityList.add("NN");
        cityList.add("KK");

        areaList.add(getString(R.string.choose_area));
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
        iv_back.setOnClickListener(this);
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

        String province,city,area;
        if(provinceSpinner.getSelectedItemPosition() == 0){
            province = null;
        }else{
            province = (String)provinceSpinner.getSelectedItem();
        }
        if(citySpinner.getSelectedItemPosition() == 0){
            city = null;
        }else {
            city = (String)citySpinner.getSelectedItem();
        }
        if(areaSpinner.getSelectedItemPosition() == 0){
            area = null;
        }else{
            area = (String)areaSpinner.getSelectedItem();
        }

        String address = et_full_address.getText().toString().trim();  //详细地址
        final String contact = et_contact.getText().toString().trim();  //联系人姓名
        String contactPhone = et_contactPhone.getText().toString().trim(); //联系人电话

        Bundle bundle = getIntent().getExtras();
        String fullname = bundle.getString("company");
        String businessLicense = bundle.getString("company_number");
        String corporationName = bundle.getString("name");
        String corporationIdNo = bundle.getString("card_number");
        String bussinessLicensePic = bundle.getString("business_pic_url");
        String corporationIdPicA = bundle.getString("corporation_pic_url");
        double longitude = bundle.getDouble("longitude");
        double latitude = bundle.getDouble("latitude");

        Log.i("test","company===>"+bundle.getString("company")+" company_number===>"+bundle.getString("company_number"));
        Log.i("test","corporationName===>"+corporationName+" corporationIdNo===>"+corporationIdNo);
        Log.i("test","longitude===>"+longitude+" latitude=>"+latitude);

        if(StringUtil.isEmpty(invoiceHeader)){
            T.show(this,getString(R.string.invoice_header_content));
            return;
        }
        if(StringUtil.isEmpty(taxIdNo)){
            T.show(this,getString(R.string.taxIdNo_tips));
            return;
        }
        if(StringUtil.isEmpty(postcode)){
            T.show(this,getString(R.string.postcode_tips));
            return;
        }
        if(StringUtil.isEmpty(province)){
            T.show(this,getString(R.string.choose_province));
            return;
        }
        if(StringUtil.isEmpty(city)){
            T.show(this,getString(R.string.choose_city));
            return;
        }
        if(StringUtil.isEmpty(area)){
            T.show(this,getString(R.string.choose_area));
            return;
        }
        if(StringUtil.isEmpty(address)){
            T.show(this,getString(R.string.mail_address_empty_tips));
            return;
        }
        if(StringUtil.isEmpty(contact)){
            T.show(this,getString(R.string.contact_name_tips));
            return;
        }
        if(StringUtil.isEmpty(contactPhone)){
            T.show(this,getString(R.string.contact_phone_tips));
            return;
        }

        BasicNameValuePair bv_fullname = new BasicNameValuePair("fullname",fullname);
        BasicNameValuePair bv_businessLicense = new BasicNameValuePair("businessLicense",businessLicense);
        BasicNameValuePair bv_corporationName = new BasicNameValuePair("corporationName",corporationName);
        BasicNameValuePair bv_corporationIdNo = new BasicNameValuePair("corporationIdNo",corporationIdNo);
        BasicNameValuePair bv_bussinessLicensePic = new BasicNameValuePair("bussinessLicensePic",bussinessLicensePic);
        BasicNameValuePair bv_corporationIdPicA = new BasicNameValuePair("corporationIdPicA",corporationIdPicA);
        BasicNameValuePair bv_longitude = new BasicNameValuePair("longitude",String.valueOf(longitude));
        BasicNameValuePair bv_latitude = new BasicNameValuePair("latitude",String.valueOf(latitude));
        BasicNameValuePair bv_invoiceHeader = new BasicNameValuePair("invoiceHeader",invoiceHeader);
        BasicNameValuePair bv_taxIdNo = new BasicNameValuePair("taxIdNo",taxIdNo);
        BasicNameValuePair bv_postcode = new BasicNameValuePair("postcode",postcode);
        BasicNameValuePair bv_province = new BasicNameValuePair("province",province);
        BasicNameValuePair bv_city = new BasicNameValuePair("city",city);
        BasicNameValuePair bv_district = new BasicNameValuePair("district",area);
        BasicNameValuePair bv_address = new BasicNameValuePair("address",address);
        BasicNameValuePair bv_contact = new BasicNameValuePair("contact",contact);
        BasicNameValuePair bv_contactPhone = new BasicNameValuePair("contactPhone",contactPhone);

        List<BasicNameValuePair> mList = new ArrayList<BasicNameValuePair>();
        mList.add(bv_fullname);
        mList.add(bv_businessLicense);
        mList.add(bv_corporationName);
        mList.add(bv_corporationIdNo);
        mList.add(bv_bussinessLicensePic);
        mList.add(bv_corporationIdPicA);
        mList.add(bv_longitude);
        mList.add(bv_latitude);
        mList.add(bv_invoiceHeader);
        mList.add(bv_taxIdNo);
        mList.add(bv_postcode);
        mList.add(bv_province);
        mList.add(bv_city);
        mList.add(bv_district);
        mList.add(bv_address);
        mList.add(bv_contact);
        mList.add(bv_contactPhone);

        Http.getInstance().postTaskToken(NetURL.CORPORATION_CHECK_URL, CooperativeSubmitEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if(entity == null){
                    T.show(context,context.getString(R.string.request_failed));
                    return;
                }
                CooperativeSubmitEntity cooperativeSubmitEntity = (CooperativeSubmitEntity) entity;
                if(cooperativeSubmitEntity.isResult()){
                    showTipsDialog();
                }else{
                    T.show(context,cooperativeSubmitEntity.getMessage());
                    return;
                }
            }
        },(BasicNameValuePair[])mList.toArray(new BasicNameValuePair[mList.size()]));
    }

    public void showTipsDialog(){
        final CheckTipsDialog dialog = new CheckTipsDialog(this,R.style.TipsDialog);
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //TODO 页面跳转
            }
        });
        dialog.show();
    }
}
