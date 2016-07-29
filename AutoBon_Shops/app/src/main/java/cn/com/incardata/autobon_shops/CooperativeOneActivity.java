package cn.com.incardata.autobon_shops;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import cn.com.incardata.http.HttpClientInCar;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.response.CooperativeInfo_Data;
import cn.com.incardata.http.response.UploadPicEntity;
import cn.com.incardata.utils.SDCardUtils;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;

/**
 * Created by zhangming on 2016/3/30.
 * 合作商加盟
 */
public class CooperativeOneActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_pic_one,iv_pic_two,iv_camera_one_pic,iv_camera_two_pic,iv_back;
    private Uri imageUri,imageCropUri;
    private Button btn_submit;
    private EditText et_company,et_company_number,et_name,et_card_number;
    private Context context;
    private TextView tv_one_example_tips,tv_two_example_tips;
    private static Map<String,String> picMap;  //key为标记,value为图片的去除ip和端口的后缀地址

    private static final int CAPTURE_BUSSINESS_LICENSE_PIC_REQUEST = 1;
    private static final int CAPTURE_CORPORATION_PIC_REQUEST = 2;
    private static final int CROP_BUSSINESS_LICENSE_PIC_REQUEST = 3;
    private static final int CROP_CORPORATION_PIC_REQUEST = 4;

    private static final String BUSINESS_KEY = "busniess_key";
    private static final String CORPORATION_KEY = "corporation_key";

    private CooperativeInfo_Data extraCooperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cooperative_info_one_activity);
        init();
        initData();
    }

    private void init(){
        context = this;
        picMap = new HashMap<String,String>();

        iv_pic_one = (ImageView) findViewById(R.id.iv_pic_one);
        iv_pic_two = (ImageView) findViewById(R.id.iv_pic_two);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        iv_camera_one_pic = (ImageView) findViewById(R.id.iv_camera_one_pic);
        iv_camera_two_pic = (ImageView) findViewById(R.id.iv_camera_two_pic);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        et_company = (EditText) findViewById(R.id.et_company);
        et_company_number = (EditText) findViewById(R.id.et_company_number);
        et_name = (EditText) findViewById(R.id.et_name);
        et_card_number = (EditText) findViewById(R.id.et_card_number);

        tv_one_example_tips = (TextView) findViewById(R.id.tv_one_example_tips);
        tv_two_example_tips = (TextView) findViewById(R.id.tv_two_example_tips);

        iv_back.setOnClickListener(this);
        iv_pic_one.setOnClickListener(this);
        iv_pic_two.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        if (getIntent().getExtras() != null){
            this.extraCooperation = getIntent().getExtras().getParcelable(MainReviewActivity.Cooperation);
        }else {
            this.extraCooperation = new CooperativeInfo_Data();
        }
    }

    private void initData(){
        if (this.extraCooperation != null){
            et_company.setText(extraCooperation.getFullname());
            et_company_number.setText(extraCooperation.getBusinessLicense());
            et_name.setText(extraCooperation.getCorporationName());
            et_card_number.setText(extraCooperation.getCorporationIdNo());

            picMap.put(BUSINESS_KEY, extraCooperation.getBussinessLicensePic());
            picMap.put(CORPORATION_KEY, extraCooperation.getCorporationIdPicA());

            if (!TextUtils.isEmpty(picMap.get(BUSINESS_KEY))){
                tv_one_example_tips.setVisibility(View.INVISIBLE);
                ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + picMap.get(BUSINESS_KEY), iv_pic_one);
            }
            if (!TextUtils.isEmpty(picMap.get(CORPORATION_KEY))){
                tv_one_example_tips.setVisibility(View.INVISIBLE);
                ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + picMap.get(CORPORATION_KEY), iv_pic_two);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_pic_one:
                onClickPic(CAPTURE_BUSSINESS_LICENSE_PIC_REQUEST);
                break;
            case R.id.iv_pic_two:
                onClickPic(CAPTURE_CORPORATION_PIC_REQUEST);
                break;
            case R.id.btn_submit: //填写信息,转移数据
                checkInfo();
                break;
        }
    }

    private void onClickPic(int requestCode){
        if (!SDCardUtils.isExistSDCard()){
            T.show(this, R.string.uninstalled_sdcard);
            return;
        }
        if (imageUri == null) {
            File file1 = new File(SDCardUtils.getGatherDir() + File.separator + "image.jpeg");
            File file2 = new File(SDCardUtils.getGatherDir() + File.separator + "imageCropUri.jpeg");

            if (file1.exists()){
                file1.delete();
            }
            if (file2.exists()){
                file2.delete();
            }
            imageUri = Uri.fromFile(file1); //初始图片uri
            imageCropUri = Uri.fromFile(file2);  //裁切后图片uri
        }
        InputMethodManager manager = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
        boolean isOpen = manager.isActive();
        if(isOpen){  //软键盘处于开状态
            manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);  //强制隐藏软键盘
        }
        capture(requestCode, imageUri);
    }

    private void capture(int requestCode, Uri imageUri){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == CAPTURE_BUSSINESS_LICENSE_PIC_REQUEST || requestCode == CAPTURE_CORPORATION_PIC_REQUEST){
            crop(requestCode);
            return;
        }
        if (requestCode == CROP_BUSSINESS_LICENSE_PIC_REQUEST){
            uploadPic(NetURL.BUSNIESS_LICENSE_URL, UploadPicEntity.class);
            return;
        }else if(requestCode == CROP_CORPORATION_PIC_REQUEST){
            uploadPic(NetURL.CORPORATION_PIC_URL, UploadPicEntity.class);
            return;
        }
    }

    private void crop(int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 450);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", "jpeg");
        if(requestCode == CAPTURE_BUSSINESS_LICENSE_PIC_REQUEST){  //裁切营业执照副本照片
            startActivityForResult(intent,CROP_BUSSINESS_LICENSE_PIC_REQUEST);
        }else if(requestCode == CAPTURE_CORPORATION_PIC_REQUEST){  //裁切法人身份证正面照
            startActivityForResult(intent,CROP_CORPORATION_PIC_REQUEST);
        }
    }

    /**
     * 上传图片
     * @param picUrl 请求的地址
     * @param cls 上传avaBean类型
     */
    public <Ti>void uploadPic(final String picUrl,final Class<Ti>cls){
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String json = HttpClientInCar.uploadImage(params[0], params[1]);
                    return json;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s == null) {
                    resetDefaultStatus();
                    T.show(getContext(), getString(R.string.upload_failed_tips));
                    return;
                }else {
                    Ti obj = JSON.parseObject(s,cls);
                    UploadPicEntity entity = (UploadPicEntity) obj;
                    if(entity.isResult()){
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageCropUri));
                            String url = entity.getData();
                            if(NetURL.BUSNIESS_LICENSE_URL.equals(picUrl)){
                                tv_one_example_tips.setVisibility(View.INVISIBLE);
                                picMap.put(BUSINESS_KEY,url);
                                iv_pic_one.setImageBitmap(bitmap);
                            }else if(NetURL.CORPORATION_PIC_URL.equals(picUrl)){
                                tv_two_example_tips.setVisibility(View.INVISIBLE);
                                picMap.put(CORPORATION_KEY,url);
                                iv_pic_two.setImageBitmap(bitmap);
                            }
                        } catch (FileNotFoundException e) {
                            resetDefaultStatus();
                        }
                    }else{
                        resetDefaultStatus();
                        T.show(getContext(), getString(R.string.upload_failed_tips));
                        return;
                    }
                }
            }
        }.execute(imageCropUri.getPath(),picUrl);
    }

    private void resetDefaultStatus(){
        tv_one_example_tips.setVisibility(View.VISIBLE);
        tv_two_example_tips.setVisibility(View.VISIBLE);
    }

    private String company;
    private String company_number;
    private String name;
    private String card_number;
    private void checkInfo(){
        company = et_company.getText().toString().trim();
        company_number = et_company_number.getText().toString().trim();
        name = et_name.getText().toString().trim();
        card_number = et_card_number.getText().toString().trim();

        if(StringUtil.isEmpty(company)){
            T.show(context,context.getString(R.string.company_not_empty_tips));
            return;
        }
        if(StringUtil.isEmpty(company_number) || company_number.length() != 15){
            T.show(context,context.getString(R.string.company_number_empty_tips));
            return;
        }
        if(StringUtil.isEmpty(name)){
            T.show(context,context.getString(R.string.name_empty_tips));
            return;
        }
        if(StringUtil.isEmpty(card_number) || !card_number.matches("^(\\d{14}|\\d{17})(\\d|[xX])$")){
            T.show(context,context.getString(R.string.card_number_tips));
            return;
        }
        if(StringUtil.isEmpty(picMap.get(BUSINESS_KEY))){
            T.show(context,context.getString(R.string.bussiness_pic_no_upload));
            return;
        }
        if(StringUtil.isEmpty(picMap.get(CORPORATION_KEY))){
            T.show(context,context.getString(R.string.card_number_pic_no_upload));
            return;
        }

        this.extraCooperation.setFullname(company);
        this.extraCooperation.setBusinessLicense(company_number);
        this.extraCooperation.setCorporationName(name);
        this.extraCooperation.setCorporationIdNo(card_number);
        this.extraCooperation.setBussinessLicensePic(picMap.get(BUSINESS_KEY));
        this.extraCooperation.setCorporationIdPicA(picMap.get(CORPORATION_KEY));

        Bundle bundle = new Bundle();
        bundle.putParcelable(MainReviewActivity.Cooperation, this.extraCooperation);
        startActivity(CooperativeTwoActivity.class, bundle);
    }
}
