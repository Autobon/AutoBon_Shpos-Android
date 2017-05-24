package cn.com.incardata.autobon_shops;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.com.incardata.adapter.PictureGridAdapter;
import cn.com.incardata.fragment.ReleaseOrderSuccessDialogFragment;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.HttpClientInCar;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.CreateOrderEntity;
import cn.com.incardata.http.response.CreateOrder_Data;
import cn.com.incardata.http.response.IdPhotoEntity;
import cn.com.incardata.http.response.ListOrder_Data;
import cn.com.incardata.http.response.ListUnfinishedEntity;
import cn.com.incardata.http.response.UploadPhotoEntity;
import cn.com.incardata.utils.AutoCon;
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
    private TextView workTime,agreedEndTime;
    //    private RadioGroup skillRadioGroup;
    private Button releaseOrder;
    private Context context;
    private TextView[] textViews = new TextView[4];

    private Uri orderPhotoUri;//文件目录
    private String orderPhotoUrl;//上传成功地址
    private String workTime_str;
    private String agreedEndTime_str;

    private String remark;
    private int skillId;
    private FragmentManager fragmentManager;
    private ReleaseOrderSuccessDialogFragment tipsDialog;
    private GridView gv_order_pic;
    private PictureGridAdapter mAdapter;
    private File tempFile;
    private String fileName = "";  //my_picture目录
    private File tempDir;
    private Uri carPhotoUri;  //temp目录

    private CheckBox assignTech;//指定技师
    private boolean isWork = false;
    private boolean isFrist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        updateUnfinishOrderNum();
        initView();
        initFile();
    }

    private void initView() {
        context = this;
        unfinishedOrderTips = (TextView) findViewById(R.id.unfinished_order_tips);
//        orderImage = (ImageView) findViewById(R.id.order_image);
//        sampleImage_text = (TextView) findViewById(R.id.sample_image_text);
        remarkEdit = (EditText) findViewById(R.id.edit_remark);
        workTime = (TextView) findViewById(R.id.work_time);
        agreedEndTime = (TextView) findViewById(R.id.agreedEndTime);
//        skillRadioGroup = (RadioGroup) findViewById(R.id.skill_readio_group);
        releaseOrder = (Button) findViewById(R.id.release_order);
        assignTech = (CheckBox) findViewById(R.id.assign_tech);
        gv_order_pic = (GridView) findViewById(R.id.gv_order_pic);
        textViews[0] = (TextView) findViewById(R.id.skill_tv1);
        textViews[1] = (TextView) findViewById(R.id.skill_tv2);
        textViews[2] = (TextView) findViewById(R.id.skill_tv3);
        textViews[3] = (TextView) findViewById(R.id.skill_tv4);

        findViewById(R.id.main_personal).setOnClickListener(this);
        unfinishedOrderTips.setOnClickListener(this);
//        orderImage.setOnClickListener(this);
        workTime.setOnClickListener(this);
        agreedEndTime.setOnClickListener(this);
        releaseOrder.setOnClickListener(this);
        textViews[0].setOnClickListener(this);
        textViews[1].setOnClickListener(this);
        textViews[2].setOnClickListener(this);
        textViews[3].setOnClickListener(this);
        textViews[0].setTag(false);
        textViews[1].setTag(false);
        textViews[2].setTag(false);
        textViews[3].setTag(false);

        mAdapter = new PictureGridAdapter(this,6);
        gv_order_pic.setAdapter(mAdapter);

        gv_order_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("test", "position===>" + position + "," + "count===>" + mAdapter.getCount());
                if (position == mAdapter.getCount() - 1 && !mAdapter.isReachMax()) {
                    if (tempDir == null) {
                        tempDir = new File(SDCardUtils.getGatherDir());
                        carPhotoUri = Uri.fromFile(new File(tempDir, "car_photo.jpeg"));
                    }
                    capture(1, carPhotoUri);
                } else {
                    LinkedHashMap<Integer, String> temp = mAdapter.getPicMap();
                    if (temp == null || temp.isEmpty()) {
                        startActivity(EnlargementActivity.class);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putStringArray(EnlargementActivity.IMAGE_URL, temp.values().toArray(new String[temp.size()]));
                        bundle.putInt(EnlargementActivity.POSITION, position);
                        startActivity(EnlargementActivity.class, bundle);
                    }
                }
            }
        });

//        skillRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                skillId = group.indexOfChild(group.findViewById(checkedId));
//                skillId++;
//            }
//        });

        workTime_str = DateCompute.getInstance().getNewTime("yyyy-MM-dd HH:mm");
        agreedEndTime_str = DateCompute.getInstance().getNewTime("yyyy-MM-dd HH:mm");
        workTime.setText(workTime_str);
        agreedEndTime.setText(agreedEndTime_str);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        updateUnfinishOrderNum();
    }

    /**
     * 更新未完成订单数量
     */
    private void updateUnfinishOrderNum(){
        Http.getInstance().getTaskToken(NetURL.LIST_UNFINISHEDV2, ListUnfinishedEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null) return;
                if (entity instanceof ListUnfinishedEntity){
                    ListUnfinishedEntity listUnfinished = (ListUnfinishedEntity) entity;
                    if (listUnfinished.isStatus()){
                        ListOrder_Data listOrder_data = JSON.parseObject(listUnfinished.getMessage().toString(),ListOrder_Data.class);
                        if (listOrder_data.getTotalElements() == 0) {
                            unfinishedOrderTips.setVisibility(View.GONE);
                        }else {
                            unfinishedOrderTips.setVisibility(View.VISIBLE);
                            unfinishedOrderTips.setText(getString(R.string.unfinished_order_tips, listOrder_data.getTotalElements()));
                        }
                    }
                }
            }
        },new BasicNameValuePair("status",String.valueOf(1)), new BasicNameValuePair("page", String.valueOf(1)), new BasicNameValuePair("pageSize", String.valueOf(1)));
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
//            case R.id.order_image:
//                onClickOrderPhoto();
//                break;
            case R.id.skill_tv1:
                if ((boolean)textViews[0].getTag()){
                    textViews[0].setTag(false);
                    textViews[0].setBackgroundResource(R.drawable.skill_off);
                    textViews[0].setTextColor(getResources().getColor(R.color.gray_A3));
                }else {
                    textViews[0].setTag(true);
                    textViews[0].setBackgroundDrawable(getResources().getDrawable(R.drawable.skill_on));
                    textViews[0].setTextColor(getResources().getColor(R.color.tv_white));
                }
                break;
            case R.id.skill_tv2:
                if ((boolean)textViews[1].getTag()){
                    textViews[1].setTag(false);
                    textViews[1].setBackgroundResource(R.drawable.skill_off);
                    textViews[1].setTextColor(getResources().getColor(R.color.gray_A3));
                }else {
                    textViews[1].setTag(true);
                    textViews[1].setBackgroundResource(R.drawable.skill_on);
                    textViews[1].setTextColor(getResources().getColor(R.color.tv_white));
                }
//                T.show(MainActivity.this, "paddingLeft = " + textViews[1].getPaddingLeft());
                break;
            case R.id.skill_tv3:
                if ((boolean)textViews[2].getTag()){
                    textViews[2].setTag(false);
                    textViews[2].setBackgroundDrawable(getResources().getDrawable(R.drawable.skill_off));
                    textViews[2].setTextColor(getResources().getColor(R.color.gray_A3));
                }else {
                    textViews[2].setTag(true);
                    textViews[2].setBackgroundDrawable(getResources().getDrawable(R.drawable.skill_on));
                    textViews[2].setTextColor(getResources().getColor(R.color.tv_white));
                }
                break;
            case R.id.skill_tv4:
                if ((boolean)textViews[3].getTag()){
                    textViews[3].setTag(false);
                    textViews[3].setBackgroundDrawable(getResources().getDrawable(R.drawable.skill_off));
                    textViews[3].setTextColor(getResources().getColor(R.color.gray_A3));
                }else {
                    textViews[3].setTag(true);
                    textViews[3].setBackgroundDrawable(getResources().getDrawable(R.drawable.skill_on));
                    textViews[3].setTextColor(getResources().getColor(R.color.tv_white));
                }
//                T.show(MainActivity.this, "paddingLeft = " + textViews[1].getPaddingLeft());
                break;
            case R.id.work_time:
                InputMethodManager manager = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
                boolean isOpen = manager.isActive();
                if (isOpen) {  //软键盘处于开状态
                    manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);  //强制隐藏软键盘
                }
                isWork = true;
                showTimePop(workTime);
                break;
            case R.id.agreedEndTime:
                InputMethodManager manager1 = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
                boolean isOpen1 = manager1.isActive();
                if (isOpen1) {  //软键盘处于开状态
                    manager1.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);  //强制隐藏软键盘
                }
                isWork = false;
                showTimePop(agreedEndTime);
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
        String project = "";
        if ((boolean)textViews[0].getTag()){
            project = project + "1,";
        }
        if ((boolean)textViews[1].getTag()){
            project = project + "2,";
        }
        if ((boolean)textViews[2].getTag()){
            project = project + "3,";
        }
        if ((boolean)textViews[3].getTag()){
            project = project + "4,";
        }
        if (project != "" && project.length() > 0){
            project = project.substring(0,project.length() - 1);
        }
        if (project.length() < 1 || project == ""){
            T.show(context, "请至少选择一项工作项目");
            return;
        }
        Map<Integer, String> picMap = mAdapter.getPicMap();
        if (picMap.size() < 1) {  //图片数量为0,提示用户
            T.show(context, "请上传至少一张图片");
            return;
        }
        Collection<String> colUrls = picMap.values();
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = colUrls.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next()).append(",");
        }
        String urls = sb.toString();
        urls = urls.substring(0, urls.length() - 1);
        Log.i("test", "urls======>" + urls);

        workTime_str = workTime.getText().toString().trim();
        if (TextUtils.isEmpty(workTime_str)){
            T.show(this, R.string.required_work_time);
            return;
        }
        agreedEndTime_str = agreedEndTime.getText().toString().trim();
        if (TextUtils.isEmpty(agreedEndTime_str)){
            T.show(this, R.string.agreedEndTime_str);
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = format.parse(workTime_str);
            Date date1 = format.parse(agreedEndTime_str);
            if (date1.getTime() < date.getTime()){
                T.show(this, "交车时间不能早于施工时间");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        remark = remarkEdit.getText().toString().trim();

        List params = new ArrayList<>();
        params.add(new BasicNameValuePair("photo", urls));
        params.add(new BasicNameValuePair("remark", remark));
        params.add(new BasicNameValuePair("agreedStartTime", workTime_str));
        params.add(new BasicNameValuePair("agreedEndTime", agreedEndTime_str));
        params.add(new BasicNameValuePair("type", project));
        params.add(new BasicNameValuePair("pushToAll", String.valueOf(!assignTech.isChecked())));
        showDialog(getString(R.string.submiting));
        Http.getInstance().postTaskToken(NetURL.CREATE_ORDERV2, CreateOrderEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(getContext(), getString(R.string.release_order_failed_again));
                    return;
                }
                if (entity instanceof CreateOrderEntity){
                    CreateOrderEntity createOrder = (CreateOrderEntity) entity;
                    if (createOrder.isStatus()){
                        CreateOrder_Data createOrder_data = JSON.parseObject(createOrder.getMessage().toString(),CreateOrder_Data.class);
                        if (assignTech.isChecked()){
                            Intent intent = new Intent(getContext(),AddContactActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putInt(AutoCon.ORDER_ID,createOrder_data.getId());
                            intent.putExtra(AutoCon.ORDER_ID, createOrder_data.getId());
                            intent.putExtra("activityId",1);
//                            bundle.putString("lon", createOrder_data.getPositionLon());
//                            bundle.putString("lat", createOrder_data.getPositionLat());
//                            intent.putExtras(bundle);
                            startActivity(intent);
                            revert();
                            return;
                        }
                        if (tipsDialog == null){
                            tipsDialog = new ReleaseOrderSuccessDialogFragment();
                            tipsDialog.setOnDismissListener(new ReleaseOrderSuccessDialogFragment.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    revert();
                                    updateUnfinishOrderNum();
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
//        orderPhotoUrl = null;
//        sampleImage_text.setVisibility(View.VISIBLE);
        remarkEdit.setText(null);
        remarkEdit.clearFocus();
//        skillRadioGroup.clearCheck();
        workTime_str = DateCompute.getInstance().getNewTime("yyyy-MM-dd HH:mm");
        agreedEndTime_str = DateCompute.getInstance().getNewTime("yyyy-MM-dd HH:mm");
        workTime.setText(workTime_str);
        agreedEndTime.setText(agreedEndTime_str);
        textViews[0].setTag(false);
        textViews[1].setTag(false);
        textViews[2].setTag(false);
        textViews[3].setTag(false);
        textViews[0].setBackgroundDrawable(getResources().getDrawable(R.drawable.skill_off));
        textViews[0].setTextColor(getResources().getColor(R.color.gray_A3));
        textViews[1].setBackgroundDrawable(getResources().getDrawable(R.drawable.skill_off));
        textViews[1].setTextColor(getResources().getColor(R.color.gray_A3));
        textViews[2].setBackgroundDrawable(getResources().getDrawable(R.drawable.skill_off));
        textViews[2].setTextColor(getResources().getColor(R.color.gray_A3));
        textViews[3].setBackgroundDrawable(getResources().getDrawable(R.drawable.skill_off));
        textViews[3].setTextColor(getResources().getColor(R.color.gray_A3));
//        if (tempDir != null && tempDir.exists()) {
//            SDCardUtils.deleteAllFileInFolder(tempDir);  //销毁临时目录及文件
//        }
//        if (tempFile != null && tempFile.exists()) {
//            File dir = tempFile.getParentFile();
//            SDCardUtils.deleteAllFileInFolder(dir);
//            Log.i("test", "dir===>" + dir.getPath());
//            tempFile = null;
//        }
        final int num = mAdapter.getPicMap().size();
        for (int i = num - 1 ; i >= 0 ; i--){
            mAdapter.removePic(i);
        }
        mAdapter.notifyDataSetChanged();
        isFrist = false;


//        if (mAdapter != null) {
//            mAdapter.onDestory();
//            mAdapter.notifyDataSetChanged();
//            System.gc();
//        }

//        mAdapter = new PictureGridAdapter(context,9);
//        gv_order_pic = (GridView) findViewById(R.id.gv_order_pic);
        assignTech.setChecked(false);
    }

//    private void onClickOrderPhoto() {
//        if (!SDCardUtils.isExistSDCard()){
//            T.show(this, R.string.uninstalled_sdcard);
//            return;
//        }
//
//        if (orderPhotoUri == null) {
//            orderPhotoUri = Uri.fromFile(new File(SDCardUtils.getGatherDir() + File.separator + "orderPhoto_s.jpeg"));
//        }
//
//        capture(GatherImage.CAPTURE_ORDER_REQUEST, orderPhotoUri);
//    }

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
//            case GatherImage.CAPTURE_ORDER_REQUEST:
//                orderPhotoProcess(orderPhotoUri);
//                break;
            case 1:
                try {
                    Bitmap bitmap = BitmapHelper.resizeImage(getContext(), carPhotoUri, 0.35f);
                    Uri uri = Uri.fromFile(tempFile);
                    boolean isSuccess = BitmapHelper.saveBitmap(uri, bitmap);

                    if (isSuccess) {  //成功保存后上传压缩后的图片
                        if (NetWorkHelper.isNetworkAvailable(context)) {
                            uploadCarPhoto(uri);
                        } else {
                            T.show(context, context.getString(R.string.no_network_tips));
                            return;
                        }
                    }
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void uploadCarPhoto(final Uri carCompressUri) {
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
                    T.show(context, getString(R.string.upload_image_failed));
                    return;
                } else {
                    IdPhotoEntity idPhotoEntity = JSON.parseObject(s, IdPhotoEntity.class);
                    if (idPhotoEntity.isStatus()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(Uri.fromFile(tempFile).getPath());
                        mAdapter.addPic(bitmap, idPhotoEntity.getMessage());  //添加图片
                        Log.i("test", "上传压缩后的图片成功,width===>" + bitmap.getWidth() + ",height===>" + bitmap.getHeight()
                                + ",size===>" + (bitmap.getByteCount() / 1024) + "KB" + ",url===>" + idPhotoEntity.getMessage());
                    } else {
                        T.show(context, getString(R.string.upload_image_failed));
                        return;
                    }
                }
            }
        }.execute(carCompressUri.getPath(), NetURL.UPLOAD_PHOTOV2);
    }

    private void initFile() {
        if (fileName.equals("")) {
            if (SDCardUtils.isExistSDCard()) {
                String path = SDCardUtils.getGatherDir() + File.separator + "my_picture";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                fileName = path + File.separator + "my_photo.jpeg";
                tempFile = new File(fileName);
            } else {
                T.show(this, getString(R.string.uninstalled_sdcard));
            }
        }
    }

//    private void orderPhotoProcess(Uri uri){
//        try {
//            Bitmap bitmap = BitmapHelper.resizeImage(getContext(), uri);
//            BitmapHelper.saveBitmap(this.orderPhotoUri, bitmap);
//            uploadOrderPhoto();
//        } catch (NullPointerException e){
//            e.printStackTrace();
//        }
//    }

//    private void uploadOrderPhoto(){
//        showDialog(getString(R.string.uploading_image));
//        new AsyncTask<String, Void, String>() {
//            @Override
//            protected String doInBackground(String... params) {
//                try {
//                    String json = HttpClientInCar.uploadImage(params[0], params[1]);
//                    return json;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                cancelDialog();
//                if (s == null) {
//                    T.show(getContext(), getString(R.string.upload_image_failed));
//                    return;
//                }else {
//                    UploadPhotoEntity photoEntity = JSON.parseObject(s, UploadPhotoEntity.class);
//                    if (photoEntity.isResult()){
//                        orderPhotoUrl = photoEntity.getData();
//                        Bitmap bitmap = BitmapFactory.decodeFile(orderPhotoUri.getPath());
//                        orderImage.setImageBitmap(bitmap);
//                        sampleImage_text.setVisibility(View.GONE);
//                    }else {
//                        T.show(getContext(), getString(R.string.upload_image_failed));
//                        return;
//                    }
//                }
//            }
//        }.execute(orderPhotoUri.getPath(), NetURL.UPLOAD_PHOTO);
//    }


    TimePopupWindow popupWindow;
    private void showTimePop(TextView textView) {
        if(popupWindow == null || !popupWindow.isShowing()){
            //时间选择器
            popupWindow = new TimePopupWindow(this, Type.ALL, textView);
            //时间选择后回调
            popupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date) {
                    if (isDateAvailable(date)) {
                        if(isWork){
                            workTime.setText(getTime(date));
                            if (!isFrist){
                                isFrist = true;
                                long time = date.getTime();
                                time = time + 3 * 60 * 60 * 1000;
                                Date date1 = new Date(time);
                                agreedEndTime.setText(getTime(date1));
                            }
                        }else {
                            if (!isFrist){
                                isFrist = true;
                            }
                            agreedEndTime.setText(getTime(date));
                        }

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
        long between = date.getTime() - System.currentTimeMillis();
//        long between = DateCompute.twoDayBetweenTime("yyyy-MM-dd HH:mm", DateCompute.getDate(date.getTime()), DateCompute.getInstance().getNewTime("yyyy-MM-dd HH:mm"));
        return between > 0 ? true : false;
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            moveTaskToBack(true);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
