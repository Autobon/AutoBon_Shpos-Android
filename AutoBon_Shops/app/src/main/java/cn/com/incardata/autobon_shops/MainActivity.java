package cn.com.incardata.autobon_shops;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.album.Album;

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
import cn.com.incardata.fragment.BaseFragment;
import cn.com.incardata.fragment.DataPlaceOrderFragment;
import cn.com.incardata.fragment.PhotographPlaceOrderFragment;
import cn.com.incardata.fragment.ReleaseOrderSuccessDialogFragment;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.HttpClientInCar;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.CreateOrderEntity;
import cn.com.incardata.http.response.IdPhotoEntity;
import cn.com.incardata.http.response.ListOrder_Data;
import cn.com.incardata.http.response.ListUnfinishedEntity;
import cn.com.incardata.permission.PermissionUtil;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.BitmapHelper;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.SDCardUtils;
import cn.com.incardata.utils.SharedPre;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.selftimeview.TimePopupWindow;
import cn.com.incardata.view.selftimeview.TimePopupWindow.Type;

/**
 * 下单
 *
 * @author wanghao
 */
public class MainActivity extends BaseForBroadcastActivity implements View.OnClickListener {

    private RelativeLayout tabRl;                           //切换的标题
    private TextView data_place_order;                      //数据下单
    private TextView photograph_place_order;                //拍照下单
    private View mainBaseline;                              //数据下单下划线
    private View secondBaseline;                            //拍照下单下划线

    private boolean isDataPlaceOrder = false;                //是否是数据下单

    private BaseFragment[] fragment = new BaseFragment[2];  //fragment数组
    private FragmentManager fragmentManager;                //fragment管理器
    private FragmentTransaction fragmentTransaction;        //操作fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        checkPermission();
        initView();
    }

    /**
     * 初始化控件及监听
     */
    private void initView() {
        tabRl = (RelativeLayout) findViewById(R.id.r2);
        data_place_order = (TextView) findViewById(R.id.data_place_order);
        photograph_place_order = (TextView) findViewById(R.id.photograph_place_order);
        mainBaseline = findViewById(R.id.main_baseline);
        secondBaseline = findViewById(R.id.second_baseline);
        int visable = SharedPre.getInt(getContext(),AutoCon.ORDER_BY_PHOTO);
        if (visable == 1){
            tabRl.setVisibility(View.VISIBLE);
        }else {
            tabRl.setVisibility(View.GONE);
        }

        findViewById(R.id.main_personal).setOnClickListener(this);

        data_place_order.setOnClickListener(this);
        photograph_place_order.setOnClickListener(this);

        switchFragment(true);
    }

    /**
     * 申请存储权限
     *
     * @param commandCode 可选指令码，用以执行指定操作
     */
    private void checkPermission(final int... commandCode) {
        permissionUtil = new PermissionUtil(this);
        permissionUtil.requestPermissions(getString(R.string.please_grant_file_location_phone_correation_authority), new PermissionUtil.PermissionListener() {
                    @Override
                    public void doAfterGrant(String... permission) {
                        Log.d("Maintivity", getString(R.string.authorization_success));
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        Log.d("Maintivity", getString(R.string.authorization_fail));
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_personal:
                startActivity(PersonalActivity.class);
                break;
            case R.id.data_place_order:
                switchFragment(true);
                break;
            case R.id.photograph_place_order:
                switchFragment(false);
                break;
        }
    }


    /**
     * 切换fragment
     *
     * @param isData
     */
    private void switchFragment(boolean isData) {
        if (isData == isDataPlaceOrder){
            return;
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        if (isData){
            if (fragment[0] == null) {
                fragment[0] = new DataPlaceOrderFragment();
            }
            if (!fragment[0].isAdded()) {
                fragmentTransaction.add(R.id.place_order_container, fragment[0]);
            }
            hideAllFragment(fragmentTransaction);
            fragmentTransaction.show(fragment[0]).commit();
            data_place_order.setTextColor(getResources().getColor(R.color.main_orange));
            mainBaseline.setVisibility(View.VISIBLE);
            photograph_place_order.setTextColor(getResources().getColor(R.color.darkgray));
            secondBaseline.setVisibility(View.INVISIBLE);

        }else {
            if (fragment[1] == null) {
                fragment[1] = new PhotographPlaceOrderFragment();
            }
            if (!fragment[1].isAdded()) {
                fragmentTransaction.add(R.id.place_order_container, fragment[1]);
            }
            hideAllFragment(fragmentTransaction);
            fragmentTransaction.show(fragment[1]).commit();
            photograph_place_order.setTextColor(getResources().getColor(R.color.main_orange));
            secondBaseline.setVisibility(View.VISIBLE);
            data_place_order.setTextColor(getResources().getColor(R.color.darkgray));
            mainBaseline.setVisibility(View.INVISIBLE);
        }
        isDataPlaceOrder = isData;
    }

    /**
     * 隐藏所有fragment
     *
     * @param ft
     */
    private void hideAllFragment(FragmentTransaction ft) {
        for (BaseFragment f : fragment) {
            if (f != null) ft.hide(f);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isDataPlaceOrder){
            if (fragment[0] != null){
                fragment[0].onActivityResult(requestCode,resultCode,data);
            }
        }else {
            if (fragment[1] != null){
                fragment[1].onActivityResult(requestCode,resultCode,data);
            }
        }
    }
}
