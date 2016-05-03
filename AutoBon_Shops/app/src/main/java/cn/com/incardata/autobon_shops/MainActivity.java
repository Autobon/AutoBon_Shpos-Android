package cn.com.incardata.autobon_shops;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.incardata.fragment.ReleaseOrderSuccessDialogFragment;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.HttpClientInCar;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.CreateOrderEntity;
import cn.com.incardata.http.response.ListUnfinishedEntity;
import cn.com.incardata.http.response.UploadPhotoEntity;
import cn.com.incardata.utils.BitmapHelper;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.GatherImage;
import cn.com.incardata.utils.SDCardUtils;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.selftimeview.TimePopupWindow;
import cn.com.incardata.view.selftimeview.TimePopupWindow.Type;

/**下单
 * @author wanghao
 */
public class MainActivity extends BaseForBroadcastActivity implements View.OnClickListener{
    private TextView unfinishedOrderTips;
    private ImageView orderImage;
    private TextView sampleImage_text;
    private EditText remarkEdit;
    private TextView workTime;
    private RadioGroup skillRadioGroup;
    private Button releaseOrder;

    private Uri orderPhotoUri;//文件目录
    private String orderPhotoUrl;//上传成功地址
    private String workTime_str;
    private String remark;
    private int skillId;
    private FragmentManager fragmentManager;
    private ReleaseOrderSuccessDialogFragment tipsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        initView();
    }

    private void initView() {
        unfinishedOrderTips = (TextView) findViewById(R.id.unfinished_order_tips);
        orderImage = (ImageView) findViewById(R.id.order_image);
        sampleImage_text = (TextView) findViewById(R.id.sample_image_text);
        remarkEdit = (EditText) findViewById(R.id.edit_remark);
        workTime = (TextView) findViewById(R.id.work_time);
        skillRadioGroup = (RadioGroup) findViewById(R.id.skill_readio_group);
        releaseOrder = (Button) findViewById(R.id.release_order);

        findViewById(R.id.main_personal).setOnClickListener(this);
        unfinishedOrderTips.setOnClickListener(this);
        orderImage.setOnClickListener(this);
        workTime.setOnClickListener(this);
        releaseOrder.setOnClickListener(this);

        skillRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                skillId = group.indexOfChild(group.findViewById(checkedId));
                skillId++;
            }
        });

        workTime_str = DateCompute.getInstance().getNewTime("yyyy-MM-dd HH:mm");
        workTime.setText(workTime_str);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUnfinishOrderNum();
    }

    /**
     * 更新未完成订单数量
     */
    private void updateUnfinishOrderNum(){
        Http.getInstance().postTaskToken(NetURL.LIST_UNFINISHED, "page=1&pageSize=0", ListUnfinishedEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null) return;
                if (entity instanceof ListUnfinishedEntity){
                    ListUnfinishedEntity listUnfinished = (ListUnfinishedEntity) entity;
                    if (listUnfinished.isResult()){
                        if (listUnfinished.getData().getTotalElements() == 0) {
                            unfinishedOrderTips.setVisibility(View.GONE);
                        }else {
                            unfinishedOrderTips.setVisibility(View.VISIBLE);
                            unfinishedOrderTips.setText(getString(R.string.unfinished_order_tips, listUnfinished.getData().getTotalElements()));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_personal:
                startActivity(PersonalActivity.class);
                break;
            case R.id.unfinished_order_tips:
                startActivity(UnfinishOrderActivity.class);
                break;
            case R.id.order_image:
                onClickOrderPhoto();
                break;
            case R.id.work_time:
                showTimePop();
                break;
            case R.id.release_order:
                releaseOrder();
                break;
        }
    }

    /**
     * 下单
     */
    private void releaseOrder() {
        if (TextUtils.isEmpty(orderPhotoUrl)){
            T.show(this, R.string.please_upload_order_photo);
            return;
        }

        workTime_str = workTime.getText().toString().trim();
        if (TextUtils.isEmpty(workTime_str)){
            T.show(this, R.string.required_work_time);
            return;
        }


        if (skillId <= 0){
            T.show(this, getString(R.string.please_order_type));
            return;
        }
        remark = remarkEdit.getText().toString().trim();

        List params = new ArrayList<>();
        params.add(new BasicNameValuePair("photo", orderPhotoUrl));
        params.add(new BasicNameValuePair("remark", remark));
        params.add(new BasicNameValuePair("orderTime", workTime_str));
        params.add(new BasicNameValuePair("orderType", String.valueOf(skillId)));
        showDialog(getString(R.string.submiting));
        Http.getInstance().postTaskToken(NetURL.CREATE_ORDER, CreateOrderEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(getContext(), getString(R.string.release_order_failed_again));
                    return;
                }
                if (entity instanceof CreateOrderEntity){
                    CreateOrderEntity createOrder = (CreateOrderEntity) entity;
                    if (createOrder.isResult()){
                        if (tipsDialog == null){
                            tipsDialog = new ReleaseOrderSuccessDialogFragment();
                            tipsDialog.setOnDismissListener(new ReleaseOrderSuccessDialogFragment.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    revert();
                                }
                            });
                        }
                        tipsDialog.show(fragmentManager, "TipsDialog");
                    }else {
                        T.show(getContext(), getString(R.string.release_order_failed_again));
                    }
                }
            }
        }, (NameValuePair[]) params.toArray(new NameValuePair[params.size()]));
    }

    /**
     * 界面恢复
     */
    private void revert(){
        orderPhotoUrl = null;
        sampleImage_text.setVisibility(View.VISIBLE);
        remarkEdit.setText(null);
        remarkEdit.clearFocus();
        skillRadioGroup.clearCheck();
        workTime_str = DateCompute.getInstance().getNewTime("yyyy-MM-dd HH:mm");
        workTime.setText(workTime_str);
    }

    private void onClickOrderPhoto() {
        if (!SDCardUtils.isExistSDCard()){
            T.show(this, R.string.uninstalled_sdcard);
            return;
        }

        if (orderPhotoUri == null) {
            orderPhotoUri = Uri.fromFile(new File(SDCardUtils.getGatherDir() + File.separator + "orderPhoto_s.jpeg"));
        }

        capture(GatherImage.CAPTURE_ORDER_REQUEST, orderPhotoUri);
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

        switch (requestCode){
            case GatherImage.CAPTURE_ORDER_REQUEST:
                orderPhotoProcess(orderPhotoUri);
                break;
        }
    }

    private void orderPhotoProcess(Uri uri){
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri));
            bitmap = BitmapHelper.resizeImage(bitmap, 0.5f);
            BitmapHelper.saveBitmap(this.orderPhotoUri, bitmap, true);
            uploadOrderPhoto();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void uploadOrderPhoto(){
        showDialog(getString(R.string.uploading_image));
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
                cancelDialog();
                if (s == null) {
                    T.show(getContext(), getString(R.string.upload_image_failed));
                    return;
                }else {
                    UploadPhotoEntity photoEntity = JSON.parseObject(s, UploadPhotoEntity.class);
                    if (photoEntity.isResult()){
                        orderPhotoUrl = photoEntity.getData();
                        Bitmap bitmap = BitmapFactory.decodeFile(orderPhotoUri.getPath());
                        orderImage.setImageBitmap(bitmap);
                        sampleImage_text.setVisibility(View.GONE);
                    }else {
                        T.show(getContext(), getString(R.string.upload_image_failed));
                        return;
                    }
                }
            }
        }.execute(orderPhotoUri.getPath(), NetURL.UPLOAD_PHOTO);
    }


    TimePopupWindow popupWindow;
    private void showTimePop() {
        if(popupWindow == null || !popupWindow.isShowing()){
            //时间选择器
            popupWindow = new TimePopupWindow(this, Type.ALL, workTime);
            //时间选择后回调
            popupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date) {
                    if (isDateAvailable(date)) {
                        workTime.setText(getTime(date));
                    }else {
                        T.show(getContext(), R.string.not_later_current_time);
                    }
                }
            });
            popupWindow.showAtLocation(workTime, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 选择的时间是否有效
     * @param date
     * @return 在当前时间之前表示有效
     */
    protected boolean isDateAvailable(Date date) {
        long between = DateCompute.twoDayBetweenTime("yyyy-MM-dd HH:mm", DateCompute.getDate(date.getTime()), workTime.getText().toString().trim());
        return between > 0 ? true : false;
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
