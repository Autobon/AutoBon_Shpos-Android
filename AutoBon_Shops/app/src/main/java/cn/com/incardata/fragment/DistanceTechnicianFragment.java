package cn.com.incardata.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.MyContactAdapter;
import cn.com.incardata.autobon_shops.BaseActivity;
import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.autobon_shops.TechnicianInfoActivity;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.GetTechList;
import cn.com.incardata.http.response.GetTechListEntity;
import cn.com.incardata.http.response.TechnicianMessage;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/**指派技师- 距离界面
 * Created by yang on 2017/5/24.
 */
public class DistanceTechnicianFragment extends BaseFragment implements View.OnClickListener,
        PullToRefreshView.OnHeaderRefreshListener,PullToRefreshView.OnFooterRefreshListener {
    private View rootView;
    private ImageView iv_clear;
    private TextView tv_search;
    private EditText et_content;
    private ListView technician_list;
    private PullToRefreshView refreshView;

    private MyContactAdapter mAdapter;
    private List<TechnicianMessage> mList;

    private int total;
    private int curPage;
    private static final int pageSize = 20;
    private boolean isList = false;
    private int orderID;
    private int activityId;


    /**
     *
     * @param orderID     运单ID
     * @param activityId  跳转界面ID
     * @return
     */
    public static DistanceTechnicianFragment newInstance(int orderID, int activityId) {
        DistanceTechnicianFragment fragment = new DistanceTechnicianFragment();
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
            rootView = inflater.inflate(R.layout.fragment_distance_technician, container, false);
            initView();
            setListener();
            getTechList(1,pageSize,false);

            mList = new ArrayList<>();
            mAdapter = new MyContactAdapter(getActivity(),mList);
            technician_list.setAdapter(mAdapter);
            mAdapter.setOrderId(orderID);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }
    public void initView(){
        total = -1;  //初始化总条目个数
        curPage = 1;  //初始化当前页
        iv_clear = (ImageView) rootView.findViewById(R.id.iv_clear);
        et_content = (EditText) rootView.findViewById(R.id.et_content);
        tv_search = (TextView) rootView.findViewById(R.id.tv_search);
        technician_list = (ListView) rootView.findViewById(R.id.technician_list);
        refreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_refresh);
        refreshView.setEnablePullTorefresh(false);
        refreshView.setVisibility(View.GONE);

        technician_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),TechnicianInfoActivity.class);
                intent.putExtra("techMessage" , mList.get(position));
                intent.putExtra("orderId",orderID);
                intent.putExtra("activityId",activityId);
                startActivity(intent);
            }
        });
    }

    public void setListener(){
        iv_clear.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        refreshView.setOnFooterRefreshListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_clear:
                et_content.setText("");
                break;
            case R.id.tv_search:  //搜索
                String content = et_content.getText().toString().trim();
//                if(StringUtil.isEmpty(content)){
//                    T.show(this,getString(R.string.hint_text_phone_name));
//                    return;
//                }
                total = -1;  //初始化总条目个数
                curPage = 1;  //初始化当前页
                mList.clear();  //清除上次的数据
                findContactByPage(content,1,pageSize,true);  //分页查询技师
                break;
        }
    }

    /**
     * 查询技师列表
     * @param page  当前第几页
     * @param pageSize 一页数量
     * @param isPullRefresh 是否上拉刷新
     */
    private void getTechList(final int page, final int pageSize, final boolean isPullRefresh){
        isList = true;
        List<BasicNameValuePair> bpList = new ArrayList<BasicNameValuePair>();
//        BasicNameValuePair param = new BasicNameValuePair("query",content);
        BasicNameValuePair two_param = new BasicNameValuePair("page",String.valueOf(page).trim());
        BasicNameValuePair three_param = new BasicNameValuePair("pageSize",String.valueOf(pageSize).trim());
//        bpList.add(param);
        bpList.add(two_param);
        bpList.add(three_param);

        if(NetWorkHelper.isNetworkAvailable(getActivity())) {
            Http.getInstance().getTaskToken(NetURL.SEARCH_LISTV2, GetTechListEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null) {
                        T.show(getActivity(), getActivity().getString(R.string.operate_failed_agen));
                        return;
                    }
                    GetTechListEntity getTechListEntity = (GetTechListEntity) entity;
                    if (getTechListEntity.isStatus()) {
                        GetTechList getTechList = JSON.parseObject(String.valueOf(getTechListEntity.getMessage()),GetTechList.class);
                        if (total == -1) {
                            total = getTechList.getTotalElements(); //获取总条目数量(只在搜索时获取总条目个数,刷新时不必再次获取)
                        }
                        List<TechnicianMessage> dataList = getTechList.getContent();
                        if (total == 0) {
                            updateData(dataList,false);
                            refreshView.setVisibility(View.GONE);
                            T.show(getActivity(), getActivity().getString(R.string.no_search_tech));
                            return;
                        }
                        updateData(dataList, isPullRefresh);
                        refreshView.setVisibility(View.VISIBLE);
                    }
                }
            }, (BasicNameValuePair[]) bpList.toArray(new BasicNameValuePair[bpList.size()]));
        }else{
            T.show(getActivity(),getString(R.string.no_network_tips));
        }
        refreshView.onFooterRefreshComplete();  //不管网络是否连接,刷新完后隐藏脚布局
    }

    /**
     * 查询技师
     * @param page  当前第几页
     * @param pageSize 一页数量
     * @param isPullRefresh 是否上拉刷新
     */
    private void findContactByPage(final String content, final int page, final int pageSize, final boolean isPullRefresh){
        isList = false;
        List<BasicNameValuePair> bpList = new ArrayList<BasicNameValuePair>();
        BasicNameValuePair param = new BasicNameValuePair("query",content);
        BasicNameValuePair two_param = new BasicNameValuePair("page",String.valueOf(page).trim());
        BasicNameValuePair three_param = new BasicNameValuePair("pageSize",String.valueOf(pageSize).trim());
        bpList.add(param);
        bpList.add(two_param);
        bpList.add(three_param);

        if(NetWorkHelper.isNetworkAvailable(getActivity())) {
            Http.getInstance().getTaskToken(NetURL.SEARCHV2, GetTechListEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null) {
                        T.show(getActivity(), getActivity().getString(R.string.operate_failed_agen));
                        return;
                    }
                    GetTechListEntity getTechListEntity = (GetTechListEntity) entity;
                    if (getTechListEntity.isStatus()) {
                        GetTechList getTechList = JSON.parseObject(getTechListEntity.getMessage().toString(),GetTechList.class);
                        if (total == -1) {
                            total = getTechList.getTotalElements(); //获取总条目数量(只在搜索时获取总条目个数,刷新时不必再次获取)
                        }
                        List<TechnicianMessage> dataList = getTechList.getContent();
                        if (total == 0) {
                            updateData(dataList,false);
                            refreshView.setVisibility(View.GONE);
                            T.show(getActivity(), getActivity().getString(R.string.no_search_tech));
                            return;
                        }
                        updateData(dataList, isPullRefresh);
                        refreshView.setVisibility(View.VISIBLE);
                    }
                }
            }, (BasicNameValuePair[]) bpList.toArray(new BasicNameValuePair[bpList.size()]));
        }else{
            T.show(getActivity(),getString(R.string.no_network_tips));
        }
        refreshView.onFooterRefreshComplete();  //不管网络是否连接,刷新完后隐藏脚布局
    }

    protected void updateData(List<TechnicianMessage> dataList,boolean isPullRefresh){
        if(mList.size() < total){
            mList.addAll(dataList);
            if(isPullRefresh){  //加载数据成功后,当前页指针加一
                curPage++;
            }
        }else if(mList.size()>0){  //排除条目数为0这种情况
            T.show(getActivity(),getActivity().getString(R.string.has_load_all_label));
        }
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (isList){
            getTechList(curPage + 1,pageSize,true);
        }else {
            String phone = et_content.getText().toString().trim();
            findContactByPage(phone,curPage+1,pageSize,true);
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {

    }
}
