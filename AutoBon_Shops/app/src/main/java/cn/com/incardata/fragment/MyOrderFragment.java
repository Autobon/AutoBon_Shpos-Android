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

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import cn.com.incardata.adapter.OrderAdapter;
import cn.com.incardata.autobon_shops.GoCommentActivity;
import cn.com.incardata.autobon_shops.OrderInfoActivity;
import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.ListOrder_Data;
import cn.com.incardata.http.response.ListUnfinishedEntity;
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
public class MyOrderFragment extends android.app.Fragment {
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
        if (getArguments() != null) {
            this.isFinished = getArguments().getBoolean("isFinished");
        }

//        if (isFinished){
//            url = NetURL.LIST_FINISHED;
//        }else {
//            url = NetURL.LIST_UNCOMMENT;
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_order, container, false);
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
                if (isFinished) {
                    getpageList(2, page,true);
                } else {
                    getpageList(3, page,true);
                }

            }
        });
        mPull.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                if (page >= totalPages) {
                    T.show(getActivity(), R.string.has_load_all_label);
                    mPull.loadedCompleted();
                    return;
                }
                if (isFinished) {
                    getpageList(2, ++page,true);
                } else {
                    getpageList(3, ++page,true);
                }
            }
        });

        mAdapter.setOnClickCommentListener(new OrderAdapter.OnClickCommentListener() {
            @Override
            public void onClickOperate(int position) {
                if ("EXPIRED".equals(mList.get(position).getStatus())) {//超时订单
                    T.show(getActivity(), R.string.timeouted_order);
                    return;
                } else if ("FINISHED".equals(mList.get(position).getStatus())) {//未评价
                    Intent intent = new Intent(getActivity(), GoCommentActivity.class);
                    intent.putExtra("UserName", mList.get(position).getTechName());
                    intent.putExtra("UserPhotoUrl", mList.get(position).getTechAvatar());
                    intent.putExtra("orderCount", mList.get(position).getOrderCount());
                    intent.putExtra("OrderId", mList.get(position).getId());
                    intent.putExtra("evaluate", mList.get(position).getEvaluate());
                    startActivityForResult(intent, RequestCode);//去评价
                } else if ("COMMENTED".equals(mList.get(position).getStatus())) {
                    T.show(getActivity(), R.string.comment_order);
                    return;
                } else if ("GIVEN_UP".equals(mList.get(position).getStatus()) || "CANCELED".equals(mList.get(position).getStatus())) {//撤单
                    T.show(getActivity(), getString(R.string.revoked_order));
                    return;
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if ("EXPIRED".equals(mList.get(position).getStatus())) {
//                    T.show(getActivity(), R.string.timeouted_order);
//                    return;
//                } else if ("GIVEN_UP".equals(mList.get(position).getStatus()) ||
//                        "CANCELED".equals(mList.get(position).getStatus())) {//超时订单
//                    T.show(getActivity(), R.string.revoked_order);
//                    return;
//                } else {
                startActivity(OrderInfoActivity.class, position);
                return;
//                }
            }
        });
        if (isFinished) {
            getpageList(2, 1,true);
        } else {
            getpageList(3, 1,false);
        }

    }

    private void startActivity(Class<?> cls, int position) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtra(AutoCon.ORDER_INFO, mList.get(position));
        startActivity(intent);
    }

    //    private void getpageList(int page) {
//        Http.getInstance().postTaskToken(url, ListFinishedEntity.class, new OnResult() {
//            @Override
//            public void onResult(Object entity) {
//                mPull.loadedCompleted();
//                if (entity == null) {
//                    T.show(getActivity(), R.string.loading_data_failed);
//                    isRefresh = false;
//                    return;
//                }
//                if (entity instanceof ListFinishedEntity) {
//                    ListFinishedEntity list = (ListFinishedEntity) entity;
//                    totalPages = list.getData().getTotalPages();
//                    if (list.isResult()) {
//                        if (isRefresh) {
//                            mList.clear();
//                        }
////                        mList.addAll(list.getData().getList());
//                        mAdapter.notifyDataSetChanged();
//                    } else {
//                        T.show(getActivity(), R.string.loading_data_failed);
//                    }
//                    isRefresh = false;
//                }
//            }
//        }, new BasicNameValuePair("page", String.valueOf(page)), new BasicNameValuePair("pageSize", "10"));
//    }
    private void getpageList(int status, int page, final boolean isFrist) {
        Http.getInstance().getTaskToken(NetURL.LIST_UNFINISHEDV2, ListUnfinishedEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                mPull.loadedCompleted();
                if (entity == null) {
                    T.show(getActivity(), R.string.loading_data_failed);
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
                        if (listOrder_data.getTotalElements() == 0){
                            if (isFrist){
                                T.show(getActivity(),R.string.no_order);
                            }
                        }
                        mList.addAll(listOrder_data.getContent());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        T.show(getActivity(), R.string.loading_data_failed);
                    }
                    isRefresh = false;
                }
            }
        }, new BasicNameValuePair("status", String.valueOf(status)), new BasicNameValuePair("page", String.valueOf(page)), new BasicNameValuePair("pageSize", "10"));
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMyOrderFragmentListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
