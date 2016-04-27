package cn.com.incardata.autobon_shops;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.UnfinishListAdapter;
import cn.com.incardata.fragment.TechnicianDialogFragment;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.GetTechnicianEntity;
import cn.com.incardata.http.response.ListUnfinishedEntity;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/**未完成订单
 * @author wanghao
 */
public class UnfinishOrderActivity extends BaseForBroadcastActivity implements View.OnClickListener{
    private FragmentManager fragmentManager;
    private TechnicianDialogFragment mDialog;

    private PullToRefreshView pullToRefreshView;
    private ListView mListView;
    private List<OrderInfo> mList;
    private UnfinishListAdapter mAdapter;

    private int orderId = -1;//订单ID－抢单
    private int page = 1;//当前是第几页
    private int totalPages;//总共多少页
    private boolean isRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unfinish_order);
        fragmentManager = getFragmentManager();
        initView();
    }

    private void initView() {
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.order_pull);
        mListView = (ListView) findViewById(R.id.unfinished_order_list);

        mList = new ArrayList<OrderInfo>();
        mAdapter = new UnfinishListAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        findViewById(R.id.iv_back).setOnClickListener(this);
        pullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                page = 1;
                isRefresh = true;
                getpageList(1);
            }
        });
        pullToRefreshView.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                if (page >= totalPages){
                    T.show(getContext(), R.string.has_load_all_label);
                    pullToRefreshView.loadedCompleted();
                    return;
                }
                getpageList(++page);
            }
        });
        mAdapter.setOnClickOwnerListener(new UnfinishListAdapter.OnClickOwnerListener() {
            @Override
            public void onClickOwner(int position) {
                showMainTechnicainInfo(position);
            }
        });

        getpageList(1);
    }

    /**
     * 显示技师信息对话框
     */
    private void showMainTechnicainInfo(int position) {
        showDialog();
        Http.getInstance().getTaskToken(NetURL.GET_TECHNICIAN, "orderId=" + mList.get(position).getId(), GetTechnicianEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity != null && entity instanceof GetTechnicianEntity){
                    GetTechnicianEntity tech = (GetTechnicianEntity) entity;
                    if (tech.isResult()){
                        mDialog = new TechnicianDialogFragment();
                        if (!mDialog.isAdded()) {
                            mDialog.show(fragmentManager, "TechInfoDialog");
                        }
                        mDialog.setData(tech.getData());
                    }
                }else {
                    T.show(getContext(), R.string.loading_data_failed);
                }
            }
        });
    }



    private void getpageList(int page) {
        Http.getInstance().postTaskToken(NetURL.LIST_UNFINISHED, "page=" + page + "&pageSize=5", ListUnfinishedEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                pullToRefreshView.loadedCompleted();
                if (entity == null){
                    T.show(getContext(), R.string.loading_data_failed);
                    isRefresh = false;
                    return;
                }
                if (entity instanceof ListUnfinishedEntity){
                    ListUnfinishedEntity list = (ListUnfinishedEntity) entity;
                    totalPages = list.getData().getTotalPages();
                    if (list.isResult()){
                        if (isRefresh) {
                            mList.clear();
                        }
                        mList.addAll(list.getData().getList());
                        mAdapter.notifyDataSetChanged();
                    }else {
                        T.show(getContext(), R.string.loading_data_failed);
                    }
                    isRefresh = false;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
