package cn.com.incardata.autobon_shops;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.UnfinishListAdapter;
import cn.com.incardata.fragment.TechnicianDialogFragment;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.GetTechnicianEntity;
import cn.com.incardata.http.response.ListOrder_Data;
import cn.com.incardata.http.response.ListUnfinishedEntity;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.RevokeOrderEntity;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.CaptureDialog;
import cn.com.incardata.view.PullToRefreshView;

/**
 * 未完成订单列表
 *
 * @author wanghao
 */
public class UnfinishOrderActivity extends BaseForBroadcastActivity implements View.OnClickListener {
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
    private CaptureDialog captureDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unfinish_order);
        fragmentManager = getFragmentManager();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        page = 1;
        isRefresh = true;
        getpageList(1);

    }

    private void initView() {
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.order_pull);
        mListView = (ListView) findViewById(R.id.unfinished_order_list);

        mList = new ArrayList<>();
        mAdapter = new UnfinishListAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_query).setOnClickListener(this);
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
                if (page >= totalPages) {
                    T.show(getContext(), R.string.has_load_all_label);
                    pullToRefreshView.loadedCompleted();
                    return;
                }
                getpageList(++page);
            }
        });
        mAdapter.setonClickOperateListener(new UnfinishListAdapter.OnClickOperateListener() {

            @Override
            public void onClickOrderOperate(final int position, int type) {
                switch (type) {
                    case 1:
                        Intent intent = new Intent(getContext(), AddContactActivity.class);
                        intent.putExtra(AutoCon.ORDER_ID, mList.get(position).getId());
                        intent.putExtra("activityId", 2);
                        startActivity(intent);
                        break;
                    case 2:
                        captureDialog = new CaptureDialog(getContext(), "确定撤销该订单！", true, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                captureDialog.dismiss();
                                revokeOrder(position);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                captureDialog.dismiss();
                            }
                        });
                        captureDialog.show();
                        break;
//                    case 3:
//                        showMainTechnicainInfo(position);
//                        break;
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UnfinishOrderActivity.this, UnFinishOrderInfoActivity.class);
                intent.putExtra("order_info", mList.get(position));
                startActivity(intent);
            }
        });

        getpageList(1);
    }

    /**
     * 撤单
     *
     * @param position
     */
    private void revokeOrder(final int position) {
        Http.getInstance().putTaskToken(NetURL.getRevokeOrderV2(mList.get(position).getId()), "", RevokeOrderEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null) {
                    T.show(getContext(), R.string.operate_failed_agen);
                    return;
                }
                if (entity instanceof RevokeOrderEntity) {
                    if (((RevokeOrderEntity) entity).isStatus()) {
                        page = 1;
                        isRefresh = true;
                        getpageList(1);
                        T.show(getContext(), getString(R.string.revoke_order_succeed));
                    } else {
                        T.show(getContext(), ((RevokeOrderEntity) entity).getMessage());
                    }
                }
            }
        });
    }

//    /**
//     * 显示技师信息对话框
//     */
//    private void showMainTechnicainInfo(int position) {
//        showDialog();
//        Http.getInstance().getTaskToken(NetURL.GET_TECHNICIAN, "orderId=" + mList.get(position).getId(), GetTechnicianEntity.class, new OnResult() {
//            @Override
//            public void onResult(Object entity) {
//                cancelDialog();
//                if (entity != null && entity instanceof GetTechnicianEntity){
//                    GetTechnicianEntity tech = (GetTechnicianEntity) entity;
//                    if (tech.isResult()){
//                        mDialog = new TechnicianDialogFragment();
//                        if (!mDialog.isAdded()) {
//                            mDialog.show(fragmentManager, "TechInfoDialog");
//                        }
//                        mDialog.setData(tech.getData());
//                    }
//                }else {
//                    T.show(getContext(), R.string.loading_data_failed);
//                }
//            }
//        });
//    }


    private void getpageList(int page) {
        if (page == 1) showDialog();
        Http.getInstance().getTaskToken(NetURL.LIST_UNFINISHEDV2, ListUnfinishedEntity.class, new OnResult() {
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
                        ListOrder_Data listOrder_data = JSON.parseObject(list.getMessage().toString(), ListOrder_Data.class);
                        totalPages = listOrder_data.getTotalPages();
                        if (isRefresh) {
                            mList.clear();
                        }
                        if (listOrder_data.getTotalElements() == 0) {
                            T.show(getContext(), getString(R.string.no_order));
                        }
                        mList.addAll(listOrder_data.getContent());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        T.show(getContext(), R.string.loading_data_failed);
                    }
                    isRefresh = false;
                }
            }
        }, new BasicNameValuePair("status", "1"), new BasicNameValuePair("page", String.valueOf(page)), new BasicNameValuePair("pageSize", "10"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_query:
                break;
        }
    }
}
