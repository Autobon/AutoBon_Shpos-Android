package cn.com.incardata.autobon_shops;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.incardata.adapter.ProductDetailedListAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AppointTechEntity;
import cn.com.incardata.http.response.BaseEntity;
import cn.com.incardata.http.response.BaseEntityTwo;
import cn.com.incardata.http.response.ListOrder_Data;
import cn.com.incardata.http.response.ListUnfinishedEntity;
import cn.com.incardata.http.response.ProductData;
import cn.com.incardata.http.response.ProductList_Data;
import cn.com.incardata.http.response.SetMealData;
import cn.com.incardata.http.response.SetMealList_Data;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.AddProductPopWindow;
import cn.com.incardata.view.PullToRefreshView;

/**
 * 产品明细界面
 * <p>Created by wangyang on 2019/9/25.</p>
 */
public class ProductDetailedActivity extends BaseActivity implements View.OnClickListener {

    private PullToRefreshView pullToRefreshView;                                //上下拉控件
    private ListView mListView;                                                 //列表控件
    private List<ProductData> mList;                                                 //产品明细数据源
    private ProductDetailedListAdapter mAdapter;                                //列表适配器

    private int page = 1;                                                       //当前是第几页
    private int totalPages;                                                     //总共多少页
    private boolean isRefresh = false;                                          //刷新时是否清除数据

    private List<SetMealData> setMealList;                                      //套餐列表数据
    private int addPosition = -1;                                               //需要添加到套餐的产品ID
    public static boolean isGetSetMeal = false;                                 //是否获取套餐数据

    Map<String, String> params = new HashMap<>();                                //查询数据时的参数列表

    public Map<String, String> getParams() {
        params.put("page", String.valueOf(page));
        params.put("pageSize", "10");
        return params;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detailed);
        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isGetSetMeal){
            isGetSetMeal = false;
            getMySetMealList();
        }
    }

    /**
     * 初始化控件及监听
     */
    private void initView() {
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.product_detailed_pull);
        mListView = (ListView) findViewById(R.id.product_detailed_list);

        mList = new ArrayList<>();
        setMealList = new ArrayList<>();
        mAdapter = new ProductDetailedListAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        mAdapter.setAddOnClickListener(new ProductDetailedListAdapter.AddOnClickListener() {
            @Override
            public void onClickAdd(int position) {
                if (setMealList == null || setMealList.size() == 0){
                    T.show(getContext(),"暂无套餐数据");
                    return;
                }
                addPosition = mList.get(position).getId();
                showSetMealPopupWindow();
            }
        });

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_my_set_meal).setOnClickListener(this);
        pullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                page = 1;
                isRefresh = true;
                getProductDetailedList(getParams());
            }
        });
        pullToRefreshView.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                if (page >= totalPages) {
                    T.show(getContext(), R.string.has_load_all_label);
                    pullToRefreshView.loadedCompleted();
                    return;
                }
                ++page;
                getProductDetailedList(getParams());
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


//        mList.add("aa");
//        mList.add("aa");
//        mList.add("aa");
//        mList.add("aa");
//        mList.add("aa");
//        mAdapter.notifyDataSetChanged();

        getProductDetailedList(getParams());
        getMySetMealList();

    }


    AddProductPopWindow pop;

    /**
     * 状态选择弹出框
     *
     */
    public void showSetMealPopupWindow() {
        if (pop == null) {

            pop = new AddProductPopWindow(this);
            pop.init();
            pop.setListener(listener);
        }

        pop.setData(setMealList, "添加至");

//        pop.setData(setMealList, "添加至", position);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    public AddProductPopWindow.OnCheckedListener listener = new AddProductPopWindow.OnCheckedListener() {
        @Override
        public void onChecked(int index) {
            addProductToSetMeal(setMealList.get(index).getId(),addPosition);
            addPosition = -1;
        }
    };


    /**
     * 添加报价产品到套餐中
     * @param setMenuId 套餐ID
     * @param offerId  产品ID
     */
    private void addProductToSetMeal(int setMenuId,int offerId){
        showDialog();
        Http.getInstance().postTaskToken(NetURL.ADDPRODUCTTOSETMEAL, AppointTechEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if(entity == null){
                    T.show(getContext(),R.string.operate_failed_agen);
                    return;
                }
                AppointTechEntity response = (AppointTechEntity) entity;
                if(response.isStatus()){
                    Log.i("test","添加产品到套餐成功");
                    T.show(getContext(), getString(R.string.add_product_to_set_meal_success));
//                    finish();
                }else{
                    T.show(getContext(),response.getMessage());
                }
            }
        }, new BasicNameValuePair("setMenuId",String.valueOf(setMenuId)), new BasicNameValuePair("offerId", String.valueOf(offerId)));


    }


    /**
     * 获取产品明细列表数据
     *
     * @param param 传递的参数
     */
    private void getProductDetailedList(Map<String, String> param) {

        List<BasicNameValuePair> paramList = new ArrayList<>();
        for (Map.Entry parama : param.entrySet()) {
            paramList.add(new BasicNameValuePair(parama.getKey().toString(), parama.getValue().toString()));
        }
        showDialog();
        Http.getInstance().getTaskToken(NetURL.SELECTPRODUCT, ListUnfinishedEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                pullToRefreshView.loadedCompleted();
                if (entity == null) {
                    T.show(getContext(), R.string.loading_data_failed);
                    isRefresh = false;
                    return;
                }
                if (entity instanceof ListUnfinishedEntity) {
                    ListUnfinishedEntity list = (ListUnfinishedEntity) entity;
                    if (list.isStatus()) {
                        if (list.getMessage() == null) {
                            T.show(getContext(), R.string.loading_data_failed);
                            isRefresh = false;
                            return;
                        }
                        ProductList_Data productList_data = JSON.parseObject(list.getMessage().toString(), ProductList_Data.class);
                        totalPages = productList_data.getTotalPages();
                        if (isRefresh) {
                            mList.clear();
                        }
                        if (productList_data.getTotalElements() == 0) {
                            T.show(getContext(), getString(R.string.no_order));
                        }
                        mList.addAll(productList_data.getList());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        T.show(getContext(), R.string.loading_data_failed);
                    }
                    isRefresh = false;
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
                pullToRefreshView.loadedCompleted();
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
                        if (setMealList != null &&setMealList.size() > 0){
                            setMealList.clear();
                        }
                        setMealList.addAll(setMealList_data.getList());
                    } else {
                        T.show(getContext(), R.string.loading_data_failed);
                    }
                }
            }
        }, (BasicNameValuePair[]) paramList.toArray(new BasicNameValuePair[paramList.size()]));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_my_set_meal:
                startActivity(MySetMealActivity.class);
                break;
        }
    }

}
