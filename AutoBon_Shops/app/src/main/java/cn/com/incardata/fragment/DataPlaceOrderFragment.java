package cn.com.incardata.fragment;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.incardata.adapter.OrderProductListAdapter;
import cn.com.incardata.adapter.OrderSetMealListAdapter;
import cn.com.incardata.adapter.OrderYetSelectSetMealListAdapter;
import cn.com.incardata.adapter.ProductDetailedListAdapter;
import cn.com.incardata.adapter.SetMealProductListAdapter;
import cn.com.incardata.autobon_shops.AllOrderListActivity;
import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.BaseEntity;
import cn.com.incardata.http.response.CreateOrderEntity;
import cn.com.incardata.http.response.ListOrder_Data;
import cn.com.incardata.http.response.ListUnfinishedEntity;
import cn.com.incardata.http.response.ProductData;
import cn.com.incardata.http.response.ProductList_Data;
import cn.com.incardata.http.response.SetMealData;
import cn.com.incardata.http.response.SetMealInfoProductList_Data;
import cn.com.incardata.http.response.SetMealList_Data;
import cn.com.incardata.utils.DateCompute;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.AddProductPopWindow;
import cn.com.incardata.view.OrderProductInfoPopupwindow;
import cn.com.incardata.view.OrderSetMealInfoPopupwindow;
import cn.com.incardata.view.OrderYetSelectProductPopupwindow;
import cn.com.incardata.view.OrderYetSelectSetMealPopWindow;
import cn.com.incardata.view.selftimeview.TimePopupWindow;

/**
 * 数据下单界面
 * <p>Created by wangyang on 2019/9/27.</p>
 */
public class DataPlaceOrderFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;                      //容器
    private EditText ed_vin;                    //车架号
    private EditText ed_license;                //车牌号
    private EditText ed_type;                   //车型
    private TextView tv_work_time;              //预约施工时间
    private TextView agreedEndTime;             //最迟交车时间
    private LinearLayout ll_switch;             //切换项目和套餐
    private ImageView img_switch;               //切换项目和套餐的图标
    private TextView tv_switch;                 //切换项目和套餐的文字
    private LinearLayout ll_product;            //施工项目总容器
    private RelativeLayout[] rl_count = new RelativeLayout[4];  //施工项目父布局
    private RelativeLayout rl_patch;            //贴膜
    private TextView tv_patch;                  //贴膜
    private TextView tv_patch_selected;         //贴膜已选数量
    private RelativeLayout rl_cosmetology;      //美容
    private TextView tv_cosmetology;            //美容
    private TextView tv_cosmetology_selected;   //美容已选数量
    private RelativeLayout rl_cover;            //车衣
    private TextView tv_cover;                  //车衣
    private TextView tv_cover_selected;         //车衣已选数量
    private RelativeLayout rl_change_color;     //改色
    private TextView tv_change_color;           //改色
    private TextView tv_change_color_selected;  //改色已选数量
    private ListView product_detailed_list;     //相应项目的报价列表
    private ListView set_meal_list;             //相应项目的报价列表
    private TextView check_selecded;            //查看已选
    private TextView release_order;             //一键下单

    private List<ProductData> productList;                //产品明细数据源
    private List<ProductData> patchProductList;           //隔热膜产品明细数据源
    private List<ProductData> coverProductList;           //隐形车衣产品明细数据源
    private List<ProductData> changeColorProductList;     //车身改色产品明细数据源
    private List<ProductData> cosmetologyProductList;     //美容清洁产品明细数据源
    private OrderProductListAdapter productAdapter;       //产品列表适配器

    private int patchSelectNum = 0;                       //隔热膜项目所选数量
    private int coverSelectNum = 0;                       //隐形车衣项目所选数量
    private int changeColorSelectNum = 0;                 //车身改色项目所选数量
    private int cosmetologySelectNum = 0;                 //美容清洁项目所选数量

    private List<ProductData> selectedProductList;            //已选报价产品列表
    private List<SetMealData> selectedSetMealList;            //已选报价套餐列表

    private List<SetMealData> setMealList;                //套餐数据源
    private OrderSetMealListAdapter setMeaAdapter;   //套餐列表适配器


    private boolean isWork = false;                  //是否是选择施工时间
    private boolean isFrist = false;                 //是否是第一次选择


    private FragmentManager fragmentManager;
    private ReleaseOrderSuccessDialogFragment tipsDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_data_place_order, container, false);
            fragmentManager = getActivity().getFragmentManager();
            initView();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    /**
     * 初始化控件及监听
     */
    private void initView() {
        ed_vin = (EditText) rootView.findViewById(R.id.ed_vin);
        ed_license = (EditText) rootView.findViewById(R.id.ed_license);
        ed_type = (EditText) rootView.findViewById(R.id.ed_type);
        tv_work_time = (TextView) rootView.findViewById(R.id.tv_work_time);
        agreedEndTime = (TextView) rootView.findViewById(R.id.agreedEndTime);
        ll_switch = (LinearLayout) rootView.findViewById(R.id.ll_switch);
        img_switch = (ImageView) rootView.findViewById(R.id.img_switch);
        tv_switch = (TextView) rootView.findViewById(R.id.tv_switch);
        ll_product = (LinearLayout) rootView.findViewById(R.id.ll_product);
        rl_count[0] = (RelativeLayout) rootView.findViewById(R.id.rl_patch);
        tv_patch = (TextView) rootView.findViewById(R.id.tv_patch);
        tv_patch_selected = (TextView) rootView.findViewById(R.id.tv_patch_selected);
        rl_count[1] = (RelativeLayout) rootView.findViewById(R.id.rl_cosmetology);
        tv_cosmetology = (TextView) rootView.findViewById(R.id.tv_cosmetology);
        tv_cosmetology_selected = (TextView) rootView.findViewById(R.id.tv_cosmetology_selected);
        rl_count[2] = (RelativeLayout) rootView.findViewById(R.id.rl_cover);
        tv_cover = (TextView) rootView.findViewById(R.id.tv_cover);
        tv_cover_selected = (TextView) rootView.findViewById(R.id.tv_cover_selected);
        rl_count[3] = (RelativeLayout) rootView.findViewById(R.id.rl_change_color);
        tv_change_color = (TextView) rootView.findViewById(R.id.tv_change_color);
        tv_change_color_selected = (TextView) rootView.findViewById(R.id.tv_change_color_selected);
        product_detailed_list = (ListView) rootView.findViewById(R.id.product_detailed_list);
        set_meal_list = (ListView) rootView.findViewById(R.id.set_meal_list);
        check_selecded = (TextView) rootView.findViewById(R.id.check_selecded);
        release_order = (TextView) rootView.findViewById(R.id.release_order);

        productList = new ArrayList<>();
        patchProductList = new ArrayList<>();
        coverProductList = new ArrayList<>();
        changeColorProductList = new ArrayList<>();
        cosmetologyProductList = new ArrayList<>();
        selectedProductList = new ArrayList<>();
        selectedSetMealList = new ArrayList<>();
        productAdapter = new OrderProductListAdapter(getContext(), productList);
        product_detailed_list.setAdapter(productAdapter);

        productAdapter.setSelectOnClickListener(new OrderProductListAdapter.SelectOnClickListener() {
            @Override
            public void onClickSelect(ProductData data) {
                selectedProductList.add(data);
                productAdapter.update(selectedProductList);
                if (currenCheckId == 0) {
                    patchSelectNum++;
                    if (patchSelectNum > 0) {
                        tv_patch_selected.setVisibility(View.VISIBLE);
                        tv_patch_selected.setText(String.valueOf(patchSelectNum));
                    } else {
                        tv_patch_selected.setVisibility(View.GONE);
                    }
                } else if (currenCheckId == 1) {
                    cosmetologySelectNum++;
                    if (cosmetologySelectNum > 0) {
                        tv_cosmetology_selected.setVisibility(View.VISIBLE);
                        tv_cosmetology_selected.setText(String.valueOf(cosmetologySelectNum));
                    } else {
                        tv_cosmetology_selected.setVisibility(View.GONE);
                    }
                } else if (currenCheckId == 2) {
                    coverSelectNum++;
                    if (coverSelectNum > 0) {
                        tv_cover_selected.setVisibility(View.VISIBLE);
                        tv_cover_selected.setText(String.valueOf(coverSelectNum));
                    } else {
                        tv_cover_selected.setVisibility(View.GONE);
                    }
                } else if (currenCheckId == 3) {
                    changeColorSelectNum++;
                    if (changeColorSelectNum > 0) {
                        tv_change_color_selected.setVisibility(View.VISIBLE);
                        tv_change_color_selected.setText(String.valueOf(changeColorSelectNum));
                    } else {
                        tv_change_color_selected.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onClickDelete(ProductData data) {

                for (int i = 0; i < selectedProductList.size(); i++) {
                    if (data.getId() == selectedProductList.get(i).getId()) {
                        selectedProductList.remove(i);
                        productAdapter.update(selectedProductList);
                    }
                }
                if (currenCheckId == 0) {
                    patchSelectNum--;
                    if (patchSelectNum > 0) {
                        tv_patch_selected.setVisibility(View.VISIBLE);
                        tv_patch_selected.setText(String.valueOf(patchSelectNum));
                    } else {
                        tv_patch_selected.setVisibility(View.GONE);
                    }
                } else if (currenCheckId == 1) {
                    cosmetologySelectNum--;
                    if (cosmetologySelectNum > 0) {
                        tv_cosmetology_selected.setVisibility(View.VISIBLE);
                        tv_cosmetology_selected.setText(String.valueOf(cosmetologySelectNum));
                    } else {
                        tv_cosmetology_selected.setVisibility(View.GONE);
                    }
                } else if (currenCheckId == 2) {
                    coverSelectNum--;
                    if (coverSelectNum > 0) {
                        tv_cover_selected.setVisibility(View.VISIBLE);
                        tv_cover_selected.setText(String.valueOf(coverSelectNum));
                    } else {
                        tv_cover_selected.setVisibility(View.GONE);
                    }
                } else if (currenCheckId == 3) {
                    changeColorSelectNum--;
                    if (changeColorSelectNum > 0) {
                        tv_change_color_selected.setVisibility(View.VISIBLE);
                        tv_change_color_selected.setText(String.valueOf(changeColorSelectNum));
                    } else {
                        tv_change_color_selected.setVisibility(View.GONE);
                    }
                }
            }
        });

        product_detailed_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currenCheckId == 0) {
                    showOrderProductInfoPopupWindow(patchProductList.get(position));
                } else if (currenCheckId == 1) {
                    showOrderProductInfoPopupWindow(cosmetologyProductList.get(position));
                } else if (currenCheckId == 2) {
                    showOrderProductInfoPopupWindow(coverProductList.get(position));
                } else if (currenCheckId == 3) {
                    showOrderProductInfoPopupWindow(changeColorProductList.get(position));
                }
            }
        });


        setMealList = new ArrayList<>();
        setMeaAdapter = new OrderSetMealListAdapter(getContext(), setMealList);
        set_meal_list.setAdapter(setMeaAdapter);

        setMeaAdapter.setSelectOnClickListener(new OrderSetMealListAdapter.SelectOnClickListener() {
            @Override
            public void onClickSelect(SetMealData data) {
                if (selectedSetMealList != null && selectedSetMealList.size() > 0) {
                    selectedSetMealList.clear();
                }
                selectedSetMealList.add(data);
                setMeaAdapter.setData(selectedSetMealList);
            }

            @Override
            public void onClickDelete(SetMealData data) {
                selectedSetMealList.remove(0);
                setMeaAdapter.setData(selectedSetMealList);
            }
        });

        set_meal_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getSetMealInfo(setMealList.get(position).getId());
            }
        });


        updateProjectStyle(0);
        switchProjectAndSetMeal(true);
        tv_work_time.setOnClickListener(this);
        agreedEndTime.setOnClickListener(this);
        ll_switch.setOnClickListener(this);
        for (RelativeLayout rl : rl_count) {
            rl.setOnClickListener(this);
        }
//        rl_patch.setOnClickListener(this);
//        rl_cosmetology.setOnClickListener(this);
//        rl_cover.setOnClickListener(this);
//        rl_change_color.setOnClickListener(this);
        check_selecded.setOnClickListener(this);
        release_order.setOnClickListener(this);

        getProductDetailedList(1);
        getMySetMealList();
    }

    /**
     * 控件点击事件回调
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_work_time:
                InputMethodManager manager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                boolean isOpen = manager.isActive();
                if (isOpen) {  //软键盘处于开状态
                    getView().requestFocus();
                    manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);  //强制隐藏软键盘
                }
                isWork = true;
                showTimePop(tv_work_time);
                break;
            case R.id.agreedEndTime:
                InputMethodManager manager1 = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                boolean isOpen1 = manager1.isActive();
                if (isOpen1) {  //软键盘处于开状态
                    getView().requestFocus();
                    manager1.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);  //强制隐藏软键盘
                }
                isWork = false;
                showTimePop(agreedEndTime);
                break;
            case R.id.ll_switch:
                if (isProjects) {
                    switchProjectAndSetMeal(false);
                } else {
                    switchProjectAndSetMeal(true);
                }
                break;
            case R.id.rl_patch:
                updateProjectStyle(0);
//                getProductDetailedList(1);
                break;
            case R.id.rl_cosmetology:
                updateProjectStyle(1);
//                getProductDetailedList(4);
                break;
            case R.id.rl_cover:
                updateProjectStyle(2);
//                getProductDetailedList(2);
                break;
            case R.id.rl_change_color:
                updateProjectStyle(3);
//                getProductDetailedList(3);
                break;
            case R.id.check_selecded:
                if (isProjects) {
                    if (selectedProductList == null || selectedProductList.size() == 0) {
                        T.show(getContext(), "暂未选择报价产品");
                        return;
                    }
                    showOrderYetSelectProductPopupWindow();
                } else {
                    if (selectedSetMealList == null || selectedSetMealList.size() == 0) {
                        T.show(getContext(), "暂未选择报价套餐");
                        return;
                    }
                    showOrderYetSelectSetMealPopupWindow();
                }
                break;
            case R.id.release_order:
                releaseOrder();
                break;

        }
    }


    TimePopupWindow popupWindow;

    /**
     * 时间选择器
     *
     * @param textView
     */
    private void showTimePop(TextView textView) {
        if (popupWindow == null || !popupWindow.isShowing()) {
            //时间选择器
            popupWindow = new TimePopupWindow(getContext(), TimePopupWindow.Type.ALL, textView);
            //时间选择后回调
            popupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date) {
                    if (isDateAvailable(date)) {
                        if (isWork) {
                            tv_work_time.setText(getTime(date));
                            if (!isFrist) {
                                isFrist = true;
                                long time = date.getTime();
                                time = time + 3 * 60 * 60 * 1000;
                                Date date1 = new Date(time);
                                agreedEndTime.setText(getTime(date1));
                            }
                        } else {
                            if (!isFrist) {
                                isFrist = true;
                            }
                            agreedEndTime.setText(getTime(date));
                        }

                    } else {
                        T.show(getContext(), R.string.not_later_current_time);
                    }
                }
            });
            popupWindow.showAtLocation(tv_work_time, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 选择的时间是否有效
     *
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

    private int currenCheckId = -1;  //当前施工项目

    /**
     * 切换施工项目的布局风格
     *
     * @param checkId
     */
    private void updateProjectStyle(int checkId) {
        if (currenCheckId == checkId) {
            return;
        }
        for (int i = 0; i < rl_count.length; i++) {
            if (checkId == i) {
                rl_count[i].setBackgroundColor(getResources().getColor(R.color.tv_white));
            } else {
                rl_count[i].setBackgroundColor(getResources().getColor(R.color.order_gray));
            }
        }

        currenCheckId = checkId;

        if (checkId == 0){
            getProductDetailedList(1);
        }else if (checkId == 1){
            getProductDetailedList(4);
        }else if (checkId == 2){
            getProductDetailedList(2);
        }else if (checkId == 3){
            getProductDetailedList(3);
        }
    }

    private boolean isProjects = false; //是否是项目

    /**
     * 切换项目和套餐
     *
     * @param isProject 是否是项目
     */
    private void switchProjectAndSetMeal(boolean isProject) {
        if (isProjects == isProject) {
            return;
        }

        if (isProject) {
            ll_product.setVisibility(View.VISIBLE);
            product_detailed_list.setVisibility(View.VISIBLE);
            set_meal_list.setVisibility(View.GONE);
            tv_switch.setText(R.string.select_set_meal);
            check_selecded.setText(R.string.selected_project);
            img_switch.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.clb_btn_xztc));
            if (currenCheckId == 0){
                getProductDetailedList(1);
            }else if (currenCheckId == 1){
                getProductDetailedList(4);
            }else if (currenCheckId == 2){
                getProductDetailedList(2);
            }else if (currenCheckId == 3){
                getProductDetailedList(3);
            }else {
                getProductDetailedList(1);
            }
        } else {
            ll_product.setVisibility(View.GONE);
            product_detailed_list.setVisibility(View.GONE);
            set_meal_list.setVisibility(View.VISIBLE);
            tv_switch.setText(R.string.select_project);
            check_selecded.setText(R.string.selected_set_meal);
            img_switch.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.clb_btn_xzxm));
            getMySetMealList();
        }
        isProjects = isProject;
    }


    /**
     * 获取产品明细列表数据
     */
    private void getProductDetailedList(final int type) {
        Map<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(1));
        param.put("pageSize", "1000");
        param.put("type", String.valueOf(type));
        List<BasicNameValuePair> paramList = new ArrayList<>();
        for (Map.Entry parama : param.entrySet()) {
            paramList.add(new BasicNameValuePair(parama.getKey().toString(), parama.getValue().toString()));
        }
        showDialog();
        Http.getInstance().getTaskToken(NetURL.SELECTPRODUCT, ListUnfinishedEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
//                    T.show(getContext(), R.string.loading_data_failed);
                    return;
                }
                if (entity instanceof ListUnfinishedEntity) {
                    ListUnfinishedEntity list = (ListUnfinishedEntity) entity;
                    if (list.isStatus()) {
                        if (list.getMessage() == null) {
//                            T.show(getContext(), R.string.loading_data_failed);
                            return;
                        }
                        ProductList_Data productList_data = JSON.parseObject(list.getMessage().toString(), ProductList_Data.class);
                        if (type == 1) {
                            if (patchProductList != null && patchProductList.size() > 0) {
                                patchProductList.clear();
                            }
                            patchProductList.addAll(productList_data.getList());
                            productAdapter.setData(patchProductList);
                        } else if (type == 2) {
                            if (coverProductList != null && coverProductList.size() > 0) {
                                coverProductList.clear();
                            }
                            coverProductList.addAll(productList_data.getList());
                            productAdapter.setData(coverProductList);
                        } else if (type == 3) {
                            if (changeColorProductList != null && changeColorProductList.size() > 0) {
                                changeColorProductList.clear();
                            }
                            changeColorProductList.addAll(productList_data.getList());
                            productAdapter.setData(changeColorProductList);
                        } else if (type == 4) {
                            if (cosmetologyProductList != null && cosmetologyProductList.size() > 0) {
                                cosmetologyProductList.clear();
                            }
                            cosmetologyProductList.addAll(productList_data.getList());
                            productAdapter.setData(cosmetologyProductList);
                        }
                    } else {
                        T.show(getContext(), R.string.loading_data_failed);
                    }
                }
            }
        }, (BasicNameValuePair[]) paramList.toArray(new BasicNameValuePair[paramList.size()]));
    }


    /**
     * 获取套餐列表数据
     */
    private void getMySetMealList() {
        Map<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(1));
        param.put("pageSize", "1000");

        List<BasicNameValuePair> paramList = new ArrayList<>();
        for (Map.Entry parama : param.entrySet()) {
            paramList.add(new BasicNameValuePair(parama.getKey().toString(), parama.getValue().toString()));
        }
        Http.getInstance().getTaskToken(NetURL.SELECTSETMEAL, ListUnfinishedEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null) {
                    T.show(getContext(), R.string.loading_data_failed);
                    return;
                }
                if (entity instanceof ListUnfinishedEntity) {
                    ListUnfinishedEntity list = (ListUnfinishedEntity) entity;
                    if (list.isStatus()) {
                        if (list.getMessage() == null) {
                            T.show(getContext(), R.string.loading_data_failed);
                            return;
                        }
                        SetMealList_Data setMealList_data = JSON.parseObject(list.getMessage().toString(), SetMealList_Data.class);
                        if (setMealList != null && setMealList.size() > 0) {
                            setMealList.clear();
                        }
                        setMealList.addAll(setMealList_data.getList());
                        setMeaAdapter.notifyDataSetChanged();
                    } else {
                        T.show(getContext(), R.string.loading_data_failed);
                    }
                }
            }
        }, (BasicNameValuePair[]) paramList.toArray(new BasicNameValuePair[paramList.size()]));
    }


    /**
     * 获取套餐详情数据
     */
    private void getSetMealInfo(int id) {
        showDialog();
        Http.getInstance().getTaskToken(NetURL.selectSetMealInfo(id), ListUnfinishedEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(getContext(), R.string.loading_data_failed);
                    return;
                }
                if (entity instanceof ListUnfinishedEntity) {
                    ListUnfinishedEntity list = (ListUnfinishedEntity) entity;
                    if (list.isStatus()) {
                        if (list.getMessage() == null) {
                            T.show(getContext(), R.string.loading_data_failed);
                            return;
                        }
                        List<ProductData> listData = new ArrayList<>();
                        SetMealInfoProductList_Data setMealInfoProductList_data = JSON.parseObject(list.getMessage().toString(), SetMealInfoProductList_Data.class);
                        if (setMealInfoProductList_data.getProductOffers() != null) {
                            listData.addAll(setMealInfoProductList_data.getProductOffers());
                        }

                        showOrderSetMealInfoPopupWindow(setMealInfoProductList_data.getName(), listData);

                    } else {
                        T.show(getContext(), R.string.loading_data_failed);
                    }
                }
            }
        });
    }


    /**
     * 下单
     */
    private void releaseOrder() {
        String vinStr = ed_vin.getText().toString().trim();
        if (TextUtils.isEmpty(vinStr)) {
            T.show(getContext(), R.string.please_input_vin);
            return;
        }
        String licenseStr = ed_license.getText().toString().trim();
        if (TextUtils.isEmpty(licenseStr)) {
            T.show(getContext(), R.string.please_input_license);
            return;
        }
        String carTypeStr = ed_type.getText().toString().trim();
        if (TextUtils.isEmpty(carTypeStr)) {
            T.show(getContext(), R.string.please_input_car_type);
            return;
        }
        String workTime_str = tv_work_time.getText().toString().trim();
        if (TextUtils.isEmpty(workTime_str)) {
            T.show(getContext(), R.string.required_work_time);
            return;
        }
        String agreedEndTime_str = agreedEndTime.getText().toString().trim();
        if (TextUtils.isEmpty(agreedEndTime_str)) {
            T.show(getContext(), R.string.agreedEndTime_str);
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = format.parse(workTime_str);
            Date date1 = format.parse(agreedEndTime_str);
            if (date1.getTime() < date.getTime()) {
                T.show(getContext(), "交车时间不能早于施工时间");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (isProjects) {
            if (selectedProductList == null || selectedProductList.size() <= 0) {
                T.show(getContext(), "请选择报价产品");
                return;
            }
        } else {
            if (selectedSetMealList == null || selectedSetMealList.size() <= 0) {
                T.show(getContext(), "请选择报价套餐");
                return;
            }
        }

        List params = new ArrayList<>();

        params.add(new BasicNameValuePair("agreedStartTime", workTime_str));
        params.add(new BasicNameValuePair("agreedEndTime", agreedEndTime_str));
        params.add(new BasicNameValuePair("vin", vinStr));
        params.add(new BasicNameValuePair("license", licenseStr));
        params.add(new BasicNameValuePair("vehicleModel", carTypeStr));
        String offids = "";
        if (isProjects) {
            for (int i = 0; i < selectedProductList.size(); i++) {
                offids = offids + selectedProductList.get(i).getId() + ",";
            }
            offids = offids.substring(0, offids.length() - 1);
        } else {
            offids = selectedSetMealList.get(0).getProductOfferIds();
        }
        params.add(new BasicNameValuePair("offerIds", offids));
        params.add(new BasicNameValuePair("pushToAll", String.valueOf(true)));
        showDialog(getString(R.string.submiting));
        Http.getInstance().postTaskToken(NetURL.CREATEORDERFROMDATA, CreateOrderEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(getContext(), getString(R.string.release_order_failed_again));
                    return;
                }
                if (entity instanceof CreateOrderEntity) {
                    CreateOrderEntity createOrder = (CreateOrderEntity) entity;
                    if (createOrder.isStatus()) {
//                        CreateOrder_Data createOrder_data = JSON.parseObject(createOrder.getMessage().toString(),CreateOrder_Data.class);
                        if (tipsDialog == null) {
                            tipsDialog = new ReleaseOrderSuccessDialogFragment();
                            tipsDialog.setOnDismissListener(new ReleaseOrderSuccessDialogFragment.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    revert();
                                }
                            });
                        }
                        tipsDialog.show(fragmentManager, "TipsDialog");
                    } else {
                        T.show(getContext(), createOrder.getMessage().toString());
                    }
                }
            }
        }, (NameValuePair[]) params.toArray(new NameValuePair[params.size()]));
    }

    /**
     * 界面恢复
     */
    private void revert() {
        ed_vin.setText("");
        ed_license.setText("");
        ed_type.setText("");

//        workTime_str = DateCompute.getInstance().getNewTime("yyyy-MM-dd HH:mm");
//        agreedEndTime_str = DateCompute.getInstance().getNewTime("yyyy-MM-dd HH:mm");
//        tv_work_time.setText(workTime_str);
        tv_work_time.setText("");
        agreedEndTime.setText("");
        isFrist = false;

        if (selectedProductList != null){
            selectedProductList.clear();
            productAdapter.update(selectedProductList);
        }
        if (selectedSetMealList != null){
            selectedSetMealList.clear();
            setMeaAdapter.setData(selectedSetMealList);
        }

        patchSelectNum = 0;
        tv_patch_selected.setVisibility(View.GONE);
        cosmetologySelectNum = 0;
        tv_cosmetology_selected.setVisibility(View.GONE);
        coverSelectNum = 0;
        tv_cover_selected.setVisibility(View.GONE);
        changeColorSelectNum = 0;
        tv_change_color_selected.setVisibility(View.GONE);

        isProjects = false;
        currenCheckId = -1;
        updateProjectStyle(0);
        switchProjectAndSetMeal(true);





    }


    OrderProductInfoPopupwindow orderProductInfoPopupwindow;

    /**
     * 查看报价详情弹出框
     */
    public void showOrderProductInfoPopupWindow(ProductData data) {
        if (orderProductInfoPopupwindow == null) {
            orderProductInfoPopupwindow = new OrderProductInfoPopupwindow(getActivity());
            orderProductInfoPopupwindow.init();
        }

        orderProductInfoPopupwindow.setData(data);

//        pop.setData(setMealList, "添加至", position);
        orderProductInfoPopupwindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }


    OrderSetMealInfoPopupwindow orderSetMealInfoPopupwindow;

    /**
     * 查看套餐详情弹出框
     */
    public void showOrderSetMealInfoPopupWindow(String name, List<ProductData> listData) {
        if (orderSetMealInfoPopupwindow == null) {

            orderSetMealInfoPopupwindow = new OrderSetMealInfoPopupwindow(getActivity());
            orderSetMealInfoPopupwindow.init();
        }

        orderSetMealInfoPopupwindow.setData(listData, name);

//        pop.setData(setMealList, "添加至", position);
        orderSetMealInfoPopupwindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }


    OrderYetSelectProductPopupwindow orderYetSelectProductPopupwindow;

    /**
     * 查看已选项目弹出框
     */
    public void showOrderYetSelectProductPopupWindow() {
        if (orderYetSelectProductPopupwindow == null) {
            orderYetSelectProductPopupwindow = new OrderYetSelectProductPopupwindow(getActivity());
            orderYetSelectProductPopupwindow.init();
        }

        orderYetSelectProductPopupwindow.setData(selectedProductList, "已选报价");
        orderYetSelectProductPopupwindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }


    OrderYetSelectSetMealPopWindow orderYetSelectSetMealPopWindow;

    /**
     * 查看已选套餐弹出框
     */
    public void showOrderYetSelectSetMealPopupWindow() {
        if (orderYetSelectSetMealPopWindow == null) {
            orderYetSelectSetMealPopWindow = new OrderYetSelectSetMealPopWindow(getActivity());
            orderYetSelectSetMealPopWindow.init();
        }

        orderYetSelectSetMealPopWindow.setData(selectedSetMealList, "已选套餐");

        orderYetSelectSetMealPopWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }


}
