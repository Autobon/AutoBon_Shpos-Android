package cn.com.incardata.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import cn.com.incardata.adapter.OrderAdapter;
import cn.com.incardata.autobon_shops.GoCommentActivity;
import cn.com.incardata.autobon_shops.OrderInfoActivity;
import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.ListFinishedEntity;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/**
 * 我的订单
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnMyOrderFragmentListener} interface
 * to handle interaction events.
 * Use the {@link MyOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrderFragment extends BaseFragment {
    private static final int RequestCode = 0x11;
    private OnMyOrderFragmentListener mListener;
    private View rootView;
    private boolean isFinished;
    private String url;

    private PullToRefreshView mPull;
    private ListView mListView;
    private OrderAdapter mAdapter;
    private ArrayList<OrderInfo> mList;

    private int page = 1;//当前是第几页
    private int totalPages;//总共多少页
    private boolean isRefresh = false;

    public MyOrderFragment() {
        // Required empty public constructor
    }


    /**
     * @param isFinished 全部订单
     * @return
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrderFragment newInstance(boolean isFinished) {
        MyOrderFragment fragment = new MyOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFinished", isFinished);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            this.isFinished = getArguments().getBoolean("isFinished");
        }

        if (isFinished){
            url = NetURL.LIST_FINISHED;
        }else {
            url = NetURL.LIST_UNCOMMENT;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_order, container, false);
            initView();
        }else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null){
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    private void initView() {
        mPull = (PullToRefreshView) rootView.findViewById(R.id.order_pull);
        mListView = (ListView) rootView.findViewById(R.id.finished_order_list);

        mList = new ArrayList<OrderInfo>();
        mAdapter = new OrderAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);

        mPull.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                page = 1;
                isRefresh = true;
                getpageList(1);
            }
        });
        mPull.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                if (page >= totalPages){
                    T.show(getActivity(), R.string.has_load_all_label);
                    mPull.loadedCompleted();
                    return;
                }
                getpageList(++page);
            }
        });

        mAdapter.setOnClickCommentListener(new OrderAdapter.OnClickCommentListener() {
            @Override
            public void onClickOperate(int position) {
                //去评价
                if ("EXPIRED".equals(mList.get(position).getStatus())){//超时订单
                    T.show(getActivity(), R.string.timeouted_order);
                }else if (mList.get(position).getComment() == null){//未评价
                    Intent intent = new Intent(getActivity(), GoCommentActivity.class);
                    intent.putExtra("UserName", mList.get(position).getMainTech().getName());
                    intent.putExtra("UserPhotoUrl", mList.get(position).getMainTech().getAvatar());
                    intent.putExtra("OrderId", mList.get(position).getId());
                    startActivityForResult(intent, RequestCode);//去评价
                }else {
                    T.show(getActivity(), R.string.comment_order);
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("EXPIRED".equals(mList.get(position).getStatus())){//超时订单
                    T.show(getActivity(), R.string.timeouted_order);
                    return;
                }else{
                    startActivity(OrderInfoActivity.class, position);
                }
            }
        });

        getpageList(1);
    }

    private void startActivity(Class<?> cls, int position){
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtra(AutoCon.ORDER_INFO, mList.get(position));
        startActivity(intent);
    }

    private void getpageList(int page) {
        Http.getInstance().postTaskToken(url, "page=" + page + "&pageSize=20", ListFinishedEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                mPull.loadedCompleted();
                if (entity == null) {
                    T.show(getActivity(), R.string.loading_data_failed);
                    isRefresh = false;
                    return;
                }
                if (entity instanceof ListFinishedEntity) {
                    ListFinishedEntity list = (ListFinishedEntity) entity;
                    totalPages = list.getData().getTotalPages();
                    if (list.isResult()) {
                        if (isRefresh) {
                            mList.clear();
                        }
                        mList.addAll(list.getData().getList());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        T.show(getActivity(), R.string.loading_data_failed);
                    }
                    isRefresh = false;
                }
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnMyOrderFragmentListener) {
            mListener = (OnMyOrderFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMyOrderFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMyOrderFragmentListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
