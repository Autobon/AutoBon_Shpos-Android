package cn.com.incardata.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.AssignCollectionAdapter;
import cn.com.incardata.adapter.CollectionTechnicianAdapter;
import cn.com.incardata.adapter.MyContactAdapter;
import cn.com.incardata.autobon_shops.BaseActivity;
import cn.com.incardata.autobon_shops.CollectionTechnicianInfoActivity;
import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.autobon_shops.TechnicianInfoActivity;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AppointTechEntity;
import cn.com.incardata.http.response.BaseEntity;
import cn.com.incardata.http.response.CollectionTechnician_Data;
import cn.com.incardata.http.response.GetCollectionTechnicianEntity;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/**
 * 指派技师-收藏界面
 * Created by yang on 2017/5/24.
 */
public class CollectionTechnicianFragment extends BaseFragment {

    private View rootView;
    private ListView technician_list;
    private PullToRefreshView refreshView;
    private AssignCollectionAdapter adapter;
    private List<CollectionTechnician_Data> list;

    private int page = 1;//当前是第几页
    private int totalPages;//总共多少页
    private boolean isRefresh = false;

    private int orderID;
    private int activityId;

    /**
     *
     * @param orderID     运单ID
     * @param activityId  跳转界面ID
     * @return
     */
    public static CollectionTechnicianFragment newInstance(int orderID, int activityId) {
        CollectionTechnicianFragment fragment = new CollectionTechnicianFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("orderID",orderID);
        bundle.putInt("activityId",activityId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.orderID = getArguments().getInt("orderID");
            this.activityId = getArguments().getInt("activityId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_collection_technicain, container, false);
            initView();

        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }


    private void initView() {
        list = new ArrayList<>();
        technician_list = (ListView) rootView.findViewById(R.id.technician_list);
        refreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_refresh);

        adapter = new AssignCollectionAdapter(list, getActivity());
        technician_list.setAdapter(adapter);


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
                    T.show(getActivity(), R.string.has_load_all_label);
                    refreshView.loadedCompleted();
                    return;
                }
                getCollectionTechnician(++page);
            }
        });


        adapter.setListener(new AssignCollectionAdapter.ClickListener() {
            @Override
            public void onClick(int position) {
                addTechnician(position);
            }
        });

        technician_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),CollectionTechnicianInfoActivity.class);
                intent.putExtra("techMessage" , list.get(position));
                intent.putExtra("orderId",orderID);
                intent.putExtra("activityId",activityId);
                startActivity(intent);
            }
        });

        getCollectionTechnician(1);
    }


    /**
     * 指派技师
     * @param position 被指派的技师在集合中的位置下标
     */
    public void addTechnician(final int position){
        Http.getInstance().postTaskToken(NetURL.APPOINTV2, AppointTechEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if(entity == null){
                    T.show(getActivity(),getActivity().getString(R.string.operate_failed_agen));
                    return;
                }
                AppointTechEntity tech = (AppointTechEntity) entity;
                if(tech.isStatus()){
                    Log.i("test","指定技师成功");
                    T.show(getActivity(), R.string.appoint_technician_succeed);
                    Intent i=new Intent();
                    i.putExtra("username",list.get(position).getTechnician().getName());
                    i.putExtra("technicianId",list.get(position).getTechnician().getId());
                    getActivity().setResult(getActivity().RESULT_OK,i);
                    getActivity().finish();
                }else{
                    T.show(getActivity(),tech.getMessage());
                }
            }
        }, new BasicNameValuePair("orderId", String.valueOf(orderID)), new BasicNameValuePair("techId", String.valueOf(list.get(position).getTechnician().getId())));
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
                    T.show(getActivity(), R.string.loading_data_failed);
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
                        T.show(getActivity(), "暂无收藏技师");
                    }
                    list.addAll(getCollectionTechnicianEntity.getList());
                    adapter.notifyDataSetChanged();
                    isRefresh = false;
                }
            }
        }, new BasicNameValuePair("page", String.valueOf(page)), new BasicNameValuePair("pageSize", "10"));
    }


}
