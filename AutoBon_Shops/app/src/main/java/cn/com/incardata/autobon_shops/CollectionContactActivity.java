package cn.com.incardata.autobon_shops;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.CollectionTechnicianAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.BaseEntity;
import cn.com.incardata.http.response.CollectionTechnician;
import cn.com.incardata.http.response.CollectionTechnician_Data;
import cn.com.incardata.http.response.GetCollectionTechnicianEntity;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/**
 * 已收藏的技师列表界面
 * Created by yang on 2017/5/23.
 */
public class CollectionContactActivity extends BaseActivity {
    private Context context;
    private ImageView iv_back;
    private ListView technician_list;
    private PullToRefreshView refreshView;
    private CollectionTechnicianAdapter adapter;
    private List<CollectionTechnician_Data> list;

    private int page = 1;//当前是第几页
    private int totalPages;//总共多少页
    private boolean isRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_contact);
        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        technician_list = (ListView) findViewById(R.id.technician_list);
        refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh);

        adapter = new CollectionTechnicianAdapter(list, this);
        technician_list.setAdapter(adapter);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                page = 1;
                isRefresh = true;
                getCollectionTechnician(1);
            }
        });
        refreshView.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                if (page >= totalPages) {
                    T.show(getContext(), R.string.has_load_all_label);
                    refreshView.loadedCompleted();
                    return;
                }
                getCollectionTechnician(++page);
            }
        });


        adapter.setListener(new CollectionTechnicianAdapter.ClickListener() {
            @Override
            public void onClick(int position) {
                delectCollectTechnician(position);
            }
        });

        getCollectionTechnician(1);
    }

    /**
     * 移除收藏的技师
     *
     * @param position 被移除的技师在集合中的位置
     */
    private void delectCollectTechnician(final int position) {
        showDialog();
        Http.getInstance().delTaskToken(NetURL.delectCollectionTechnician(list.get(position).getTechnician().getId()), "", BaseEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(getContext(), R.string.request_failed);
                    return;
                }
                if (entity instanceof BaseEntity) {
                    BaseEntity baseEntity = (BaseEntity) entity;
                    if (baseEntity.isResult()) {
                        T.show(getContext(), "移除技师成功");
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    } else {
                        T.show(getContext(), baseEntity.getMessage());
                    }
                }

            }
        });
    }


    /**
     * 查询已收藏的技师列表
     */
    private void getCollectionTechnician(int page) {
        Http.getInstance().getTaskToken(NetURL.SELECTCOLLECTIONTECHNICIAN, GetCollectionTechnicianEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                refreshView.loadedCompleted();
                if (entity == null) {
                    T.show(getContext(), R.string.loading_data_failed);
                    isRefresh = false;
                    return;
                }
                if (entity instanceof GetCollectionTechnicianEntity) {
                    GetCollectionTechnicianEntity getCollectionTechnicianEntity = (GetCollectionTechnicianEntity) entity;
                    totalPages = getCollectionTechnicianEntity.getTotalPages();
                    if (isRefresh) {
                        list.clear();
                    }
                    if (getCollectionTechnicianEntity.getTotalElements() == 0) {
                        T.show(getContext(), getString(R.string.no_order));
                    }
                    list.addAll(getCollectionTechnicianEntity.getList());
                    adapter.notifyDataSetChanged();
                    isRefresh = false;
                }
            }
        }, new BasicNameValuePair("page", String.valueOf(page)), new BasicNameValuePair("pageSize", "10"));
    }
}
