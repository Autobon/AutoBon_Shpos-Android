package cn.com.incardata.autobon_shops;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.NotificationListAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.MessageEntity;
import cn.com.incardata.http.response.Message_Data;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/** 通知消息
 * @author wanghao
 */
public class NotificationMessageActivity extends BaseForBroadcastActivity implements View.OnClickListener{
    private ImageView iv_back;
    private PullToRefreshView mPull;
    private NotificationListAdapter mAdapter;
    private ListView mListView;
    private List<Message_Data.Message_Data_List> mList;

    private int page = 1;//当前是第几页
    private int totalPages;//总共多少页

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_notification);
        initView();
        setListener();
    }

    private void initView(){
        iv_back = (ImageView)findViewById(R.id.iv_back);
        mListView = (ListView) findViewById(R.id.notification_list_view);
        mPull = (PullToRefreshView) findViewById(R.id.notification_list_pull);
    }

    private void setListener(){
        iv_back.setOnClickListener(this);

        mList = new ArrayList<Message_Data.Message_Data_List>();
        mAdapter = new NotificationListAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mPull.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                mList.clear();
                page = 1;
                getPageOfData(page);
            }
        });
        mPull.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                if (page >= totalPages){
                    T.show(getContext(), R.string.has_load_all_label);
                    mPull.loadedCompleted();
                    return;
                }
                getPageOfData(++page);
            }
        });
        getPageOfData(1);
    }

    private void getPageOfData(int page){
        if (page == 1) showDialog();
        Http.getInstance().getTaskToken(NetURL.MESSAGE, "page=" + page + "&pageSize=20", MessageEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                mPull.loadedCompleted();
                if (entity == null){
                    T.show(getContext(), R.string.loading_data_failed);
                    return;
                }
                if (entity instanceof MessageEntity){
                    MessageEntity message = (MessageEntity) entity;
                    totalPages = message.getData().getTotalPages();
                    if (message.isResult()){
                        mList.addAll(message.getData().getList());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
