package cn.com.incardata.autobon_shops;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.incardata.adapter.ProductDetailedListAdapter;
import cn.com.incardata.adapter.SetMealProductListAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AppointTechEntity;
import cn.com.incardata.http.response.BaseEntity;
import cn.com.incardata.http.response.BaseEntityTwo;
import cn.com.incardata.http.response.ListOrder_Data;
import cn.com.incardata.http.response.ListUnfinishedEntity;
import cn.com.incardata.http.response.ProductData;
import cn.com.incardata.http.response.SetMealInfoProductList_Data;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.AddProductPopWindow;
import cn.com.incardata.view.PullToRefreshView;

/**
 * 我的套餐详情界面
 * <p>Created by wangyang on 2019/9/26.</p>
 */
public class MySetMealInfoActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_update;                                                 //编辑按钮
//    private PullToRefreshView pullToRefreshView;                                //上下拉控件
    private ListView mListView;                                                 //列表控件
    private List<ProductData> mList;                                                 //产品明细数据源
    private SetMealProductListAdapter mAdapter;                                 //列表适配器

    private int page = 1;                                                       //当前是第几页
    private int totalPages;                                                     //总共多少页
    private boolean isRefresh = false;                                          //刷新时是否清除数据

    private boolean isUpdate = false;                                           //是否正在编辑

    private int id;                                                             //套餐id

    Map<String,String> params = new HashMap<>();                                //查询数据时的参数列表

    public Map<String, String> getParams() {
        params.put("page", String.valueOf(page));
        params.put("pageSize", "20");
        return params;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_set_meal_info);

        initView();
    }

    /**
     * 初始化控件及监听
     */
    private void initView() {
        tv_update = (TextView) findViewById(R.id.tv_update);
//        pullToRefreshView = (PullToRefreshView) findViewById(R.id.product_detailed_pull);
        mListView = (ListView) findViewById(R.id.product_detailed_list);

        mList = new ArrayList<>();
        mAdapter = new SetMealProductListAdapter(this, mList);
        mListView.setAdapter(mAdapter);

       mAdapter.setDeleteOnClickListener(new SetMealProductListAdapter.DeleteOnClickListener() {
           @Override
           public void onClickDelete(int position) {
               deleteProductToSetMeal(id,position);
           }
       });

        if (getIntent() != null){
            id = getIntent().getIntExtra("id",-1);

            if (id == -1){
                T.show(getContext(),"数据传递失败，请重试");
                finish();
            }
        }

        findViewById(R.id.iv_back).setOnClickListener(this);
        tv_update.setOnClickListener(this);
//        pullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
//            @Override
//            public void onHeaderRefresh(PullToRefreshView view) {
//                page = 1;
//                isRefresh = true;
//                getProductDetailedList(getParams());
//            }
//        });
//        pullToRefreshView.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
//            @Override
//            public void onFooterRefresh(PullToRefreshView view) {
//                if (page >= totalPages) {
//                    T.show(getContext(), R.string.has_load_all_label);
//                    pullToRefreshView.loadedCompleted();
//                    return;
//                }
//                ++page;
//                getProductDetailedList(getParams());
//            }
//        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


//        mList.add("aa");
//        mList.add("aa");
//        mList.add("aa");
//        mList.add("aa");
////        mList.add("aa");
//        mAdapter.notifyDataSetChanged();

        getSetMealInfo();
    }


    /**
     * 获取套餐详情数据
     *
     */
    private void getSetMealInfo() {
        showDialog();
        Http.getInstance().getTaskToken(NetURL.selectSetMealInfo(id), ListUnfinishedEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
//                pullToRefreshView.loadedCompleted();
                if (entity == null) {
                    T.show(getContext(), R.string.loading_data_failed);
                    isRefresh = false;
                    return;
                }
                if (entity instanceof ListUnfinishedEntity) {
                    ListUnfinishedEntity list = (ListUnfinishedEntity) entity;
                    if (list.isStatus()) {
                        if (list.getMessage() == null){
                            T.show(getContext(), R.string.loading_data_failed);
                            return;
                        }
                        SetMealInfoProductList_Data setMealInfoProductList_data = JSON.parseObject(list.getMessage().toString(), SetMealInfoProductList_Data.class);
                        if (setMealInfoProductList_data.getProductOffers() != null){
                            mList.addAll(setMealInfoProductList_data.getProductOffers());
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        T.show(getContext(), R.string.loading_data_failed);
                    }
                }
            }
        });
    }


    /**
     * 从套餐中移除报价产品
     * @param setMenuId 套餐ID
     * @param index  产品所在列表位置
     */
    private void deleteProductToSetMeal(int setMenuId, final int index){
        showDialog();
        Http.getInstance().postTaskToken(NetURL.DELETEPRODUCTTOSETMEAL, AppointTechEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if(entity == null){
                    T.show(getContext(),R.string.operate_failed_agen);
                    return;
                }
                AppointTechEntity response = (AppointTechEntity) entity;
                if(response.isStatus()){
                    Log.i("test","从套餐中删除报价产品成功");
                    T.show(getContext(), getString(R.string.delete_product_form_set_meal_success));
                    mList.remove(index);
                    mAdapter.notifyDataSetChanged();
                }else{
                    T.show(getContext(),response.getMessage());
                }
            }
        }, new BasicNameValuePair("setMenuId",String.valueOf(setMenuId)), new BasicNameValuePair("offerId", String.valueOf(mList.get(index).getId())));


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_update:
                if (mList == null || mList.size() == 0){
                    T.show(getContext(),"暂无数据提供编辑");
                    return;
                }
                isUpdate = !isUpdate;
                if (isUpdate){
                    tv_update.setText("完成");
                    if (mAdapter != null){
                        mAdapter.update(isUpdate);
                    }
                }else {
                    tv_update.setText("编辑");
                    if (mAdapter != null){
                        mAdapter.update(isUpdate);
                    }
                }
                break;
        }


    }
}
