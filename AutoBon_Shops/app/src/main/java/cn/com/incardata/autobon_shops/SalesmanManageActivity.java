package cn.com.incardata.autobon_shops;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.SalsemanAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.GetSaleListEntity;
import cn.com.incardata.http.response.SaleFiredEntity;
import cn.com.incardata.http.response.SalemanData;
import cn.com.incardata.utils.T;

/**业务员管理
 * @author wanghao
 */
public class SalesmanManageActivity extends BaseForBroadcastActivity {
    private ListView mListView;
    private SalsemanAdapter mAdapter;
    private List<SalemanData> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesman_manage);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.add_saleman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddSalemanActivity.class);
                startActivityForResult(intent, 0x12);
            }
        });

        mListView = (ListView) findViewById(R.id.salseman_list);
        mList = new ArrayList<SalemanData>();
        mAdapter = new SalsemanAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        mAdapter.setOnOperateClickListener(new SalsemanAdapter.OnOperateClickListener() {
            @Override
            public void onOperateClick(int pos, boolean isModify) {
                if (isModify){
                    Intent intent = new Intent(getContext(), AddSalemanActivity.class);
                    intent.putExtra("isModify", true);
                    intent.putExtra("Saleman", mList.get(pos));
                    startActivityForResult(intent, 0x13);
                }else {
                    if (mList.get(pos).isFired()){
                        T.show(getContext(), R.string.fired);
                    }else {
                        salemanFired(pos);
                    }
                }
            }
        });

        getSalemanList();
    }

    /**
     * 业务员离职
     */
    private void salemanFired(final int pos){
        showDialog(null);
        Http.getInstance().postTaskToken(NetURL.SALE_FIRED, SaleFiredEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null){
                    T.show(getContext(), R.string.operate_failed_agen);
                    return;
                }
                if (entity instanceof SaleFiredEntity && ((SaleFiredEntity) entity).isResult()){
                    mList.remove(pos);
                    mList.add(((SaleFiredEntity) entity).getData());
                    mAdapter.notifyDataSetInvalidated();
                }else {
                    T.show(getContext(), ((SaleFiredEntity) entity).getMessage());
                }
            }
        }, new BasicNameValuePair("coopAccountId", String.valueOf(mList.get(pos).getId())));
    }

    private void getSalemanList() {
        showDialog(getString(R.string.load_progress_tips));
        Http.getInstance().postTaskToken(NetURL.GET_SALE_LIST, "", GetSaleListEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null){
                    T.show(getContext(), R.string.loading_data_failed);
                    return;
                }
                if (entity instanceof GetSaleListEntity){
                    if (((GetSaleListEntity) entity).isResult()){
                        mList.addAll(((GetSaleListEntity) entity).getData());
                        mAdapter.notifyDataSetChanged();
                    }else {
                        T.show(getContext(), ((GetSaleListEntity) entity).getMessage());
                        return;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x12 && resultCode == RESULT_OK && data != null){
            SalemanData saleman = data.getParcelableExtra("Saleman");
            mList.add(1, saleman);
            mAdapter.notifyDataSetChanged();
        }else if (requestCode == 0x13 && resultCode == RESULT_OK && data != null){
            mList.clear();
            getSalemanList();
        }
    }
}
