package cn.com.incardata.autobon_shops;

import android.content.Intent;
import android.os.Bundle;
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

import cn.com.incardata.adapter.MySetMealListAdapter;
import cn.com.incardata.adapter.ProductDetailedListAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AppointTechEntity;
import cn.com.incardata.http.response.BaseEntity;
import cn.com.incardata.http.response.ListOrder_Data;
import cn.com.incardata.http.response.ListUnfinishedEntity;
import cn.com.incardata.http.response.SetMealData;
import cn.com.incardata.http.response.SetMealInfoProductList_Data;
import cn.com.incardata.http.response.SetMealList_Data;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.AddProductPopWindow;
import cn.com.incardata.view.PullToRefreshView;

/**
 * 我的套餐界面
 * <p>Created by wangyang on 2019/9/25.</p>
 */
public class MySetMealActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshView pullToRefreshView;                                //上下拉控件
    private ListView mListView;                                                 //列表控件
    private List<SetMealData> mList;                                                 //我的套餐列表数据源
    private MySetMealListAdapter mAdapter;                                      //列表适配器

    private int page = 1;                                                       //当前是第几页
    private int totalPages;                                                     //总共多少页
    private boolean isRefresh = false;                                          //刷新时是否清除数据

    public static boolean isRefreshed = false;

    Map<String,String> params = new HashMap<>();                                //查询数据时的参数列表

    public Map<String, String> getParams() {
        params.put("page", String.valueOf(page));
        params.put("pageSize", "20");
        return params;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_set_meal);
        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isRefreshed){
            isRefreshed = false;
            page = 1;
            isRefresh = true;
            getMySetMealList(getParams());
        }
    }

    /**
     * 初始化控件及监听
     */
    private void initView() {
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.set_meal_pull);
        mListView = (ListView) findViewById(R.id.set_meal_list);

        mList = new ArrayList<>();
        mAdapter = new MySetMealListAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        mAdapter.setDeleteOnClickListener(new MySetMealListAdapter.DeleteOnClickListener() {
            @Override
            public void onClickDelete(int position) {
                deleteSetMeal(mList.get(position).getId());
//                T.show(getContext(),"点击了第" + position + "个删除按钮" );
            }
        });

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_add_set_meal).setOnClickListener(this);
        pullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                page = 1;
                isRefresh = true;
                getMySetMealList(getParams());
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
                getMySetMealList(getParams());
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),MySetMealInfoActivity.class);
                intent.putExtra("id",mList.get(position).getId());
                startActivity(intent);
            }
        });


//        mList.add("aaaaaa");
//        mList.add("aabbbb");
//        mList.add("aaccccc");
//        mList.add("aaddddddd");
//        mList.add("aaeeeeeee");
//        mAdapter.notifyDataSetChanged();

        getMySetMealList(getParams());
    }

    /**
     * 获取套餐列表数据
     * @param param 传递的参数
     */
    private void getMySetMealList(Map<String,String> param) {

        List<BasicNameValuePair> paramList = new ArrayList<>();
        for (Map.Entry parama : param.entrySet()) {
            paramList.add(new BasicNameValuePair(parama.getKey().toString(),parama.getValue().toString()));
        }
        showDialog();
        Http.getInstance().getTaskToken(NetURL.SELECTSETMEAL, ListUnfinishedEntity.class, new OnResult() {
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
                        if (list.getMessage() == null){
                            T.show(getContext(), R.string.loading_data_failed);
                            isRefresh = false;
                            return;
                        }
                        SetMealList_Data setMealList_data  = JSON.parseObject(list.getMessage().toString(), SetMealList_Data.class);
                        totalPages = setMealList_data.getTotalPages();
                        if (isRefresh) {
                            mList.clear();
                        }
                        if (setMealList_data.getTotalElements() == 0) {
                            T.show(getContext(), getString(R.string.no_order));
                        }
                        mList.addAll(setMealList_data.getList());
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
     * 删除套餐
     * @param id
     */
    private void deleteSetMeal(int id) {
        showDialog();
        Http.getInstance().delTaskToken(NetURL.deleteSetMeal(id), AppointTechEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if(entity == null){
                    T.show(getContext(),R.string.operate_failed_agen);
                    return;
                }
                AppointTechEntity response = (AppointTechEntity) entity;
                if(response.isStatus()){
                    Log.i("test","删除套餐成功");
                    T.show(getContext(), getString(R.string.dekete_set_meal_success));
                    ProductDetailedActivity.isGetSetMeal = true;
                    page = 1;
                    isRefresh = true;
                    getMySetMealList(getParams());
                }else{
                    T.show(getContext(),response.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_add_set_meal:
                startActivity(AddSetMealActivity.class);
                break;
        }
    }
}
