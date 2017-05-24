package cn.com.incardata.autobon_shops;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.CoopCheckResultEntity;
import cn.com.incardata.http.response.CooperativeInfo_Data;
import cn.com.incardata.utils.T;

/**
 * 审核进度-加盟信息
 * @author wanghao
 */
public class MainReviewActivity extends BaseActivity implements View.OnClickListener{
    public static final String Cooperation = "Cooperation";
    private TextView coopStatus;
    private TextView failureCause;
    private TextView businessLicenseName;
    //    private TextView businessLicenseCode;
//    private TextView licenseName;
//    private TextView licenseID;
    private ImageView businessLicensePhoto;
    //    private ImageView licensePhoto;
//    private TextView invoiceHeader;
//    private TextView taxpayerCode;
//    private TextView postalCode;
//    private TextView postalAddr;
    private TextView shopsAddr;
    private Button join;

    /**
     * 来自登录（表示查看审核进度）
     */
    private boolean isFromLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_review);
        initView();
        getServiceData();
    }

    private void initView() {
        coopStatus = (TextView) findViewById(R.id.cooperation_status);
        failureCause = (TextView) findViewById(R.id.failure_cause);
        businessLicenseName = (TextView) findViewById(R.id.business_license_name);
//        businessLicenseCode = (TextView) findViewById(R.id.business_license_serial);
//        licenseName = (TextView) findViewById(R.id.licensee_name);
//        licenseID = (TextView) findViewById(R.id.licensee_serial);
        businessLicensePhoto = (ImageView) findViewById(R.id.business_license_photo);
//        licensePhoto = (ImageView) findViewById(R.id.licensee_identify_photo);
//        invoiceHeader = (TextView) findViewById(R.id.invoice_header);
//        taxpayerCode = (TextView) findViewById(R.id.taxpayer_ID);
//        postalCode = (TextView) findViewById(R.id.postal_code);
//        postalAddr = (TextView) findViewById(R.id.postal_addr);
//        shopsAddr = (TextView) findViewById(R.id.shops_addr);
        join = (Button) findViewById(R.id.join_cooperation);

        join.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        businessLicensePhoto.setOnClickListener(this);
    }

    private void getServiceData() {
        showDialog();
        Http.getInstance().getTaskToken(NetURL.COOP_CHECK_RESULTV2, "", CoopCheckResultEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(getContext(), R.string.loading_data_failed);
                    return;
                }
                if (entity instanceof CoopCheckResultEntity) {
                    CoopCheckResultEntity coop = (CoopCheckResultEntity) entity;
                    if (coop.isStatus()) {
                       CoopCheckResult_Data coopCheckResult_data = JSON.parseObject(coop.getMessage().toString(), CoopCheckResult_Data.class);
                        if (coopCheckResult_data.getCooperator() != null) {
                            showUI(coopCheckResult_data);
                        } else {
                            T.show(getContext(), R.string.loading_data_failed);
                            return;
                        }
                    } else {
                        T.show(getContext(), coop.getMessage().toString());
                        return;
                    }
                }
            }
        });
    }

    private CooperativeInfo_Data cooperation;

    private void showUI(CoopCheckResult_Data data) {
        switch (data.getCooperator().getStatusCode()) {
            case 0:// 正在审核
                coopStatus.setText(R.string.checking);
                break;
            case 1://审核成功
                break;
            case 2://审核失败
                coopStatus.setText(R.string.check_failed);
                failureCause.setVisibility(View.VISIBLE);
                failureCause.append(data.getReviewCooper().getRemark());
                join.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        this.cooperation = data.getCooperator();

        businessLicenseName.append(this.cooperation.getFullname());
//        businessLicenseCode.append(this.cooperation.getBusinessLicense());
//        licenseName.append(this.cooperation.getCorporationName());
//        licenseID.append(this.cooperation.getCorporationIdNo());
        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + this.cooperation.getBussinessLicensePic(), businessLicensePhoto);
//        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + this.cooperation.getCorporationIdPicA(), licensePhoto);
//        invoiceHeader.append(this.cooperation.getInvoiceHeader());
//        taxpayerCode.append(this.cooperation.getTaxIdNo());
//        postalCode.append(this.cooperation.getPostcode());
//        String addr = this.cooperation.getProvince() + this.cooperation.getCity() + this.cooperation.getDistrict() + this.cooperation.getAddress();
//        postalAddr.append(addr);
//        shopsAddr.append(addr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.join_cooperation:
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(Cooperation, this.cooperation);
                startActivity(CooperativeOneActivity.class);
                break;
            case R.id.business_license_photo:
                String[] photos = new String[]{this.cooperation.getBussinessLicensePic()};
                openImage(0,photos);
                break;
        }
    }

    private void openImage(int position, String... urls) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(EnlargementActivity.IMAGE_URL, urls);
        bundle.putInt(EnlargementActivity.POSITION, position);
        startActivity(EnlargementActivity.class, bundle);
    }
}
