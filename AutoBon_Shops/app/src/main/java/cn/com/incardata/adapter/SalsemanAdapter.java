package cn.com.incardata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.response.SalemanData;

/**
 * Created by wanghao on 16/4/27.
 */
public class SalsemanAdapter extends BaseAdapter implements View.OnClickListener{
    private Context context;
    private List<SalemanData> mList;

    public SalsemanAdapter(Context context, List<SalemanData> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.saleman_list_item, parent, false);
            holder = new Holder();

            holder.salemanOperate = (Button) convertView.findViewById(R.id.saleman_operate);
            holder.updateSaleman = (Button) convertView.findViewById(R.id.update_saleman);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.salemanOperate.setOnClickListener(this);
            holder.updateSaleman.setOnClickListener(this);

            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        SalemanData saleman = mList.get(position);
        holder.salemanOperate.setTag(position);
        holder.updateSaleman.setTag(position);
        if (saleman.isFired()){
            holder.salemanOperate.setText(R.string.fired);
        }else {
            holder.salemanOperate.setText(R.string.dimission);
        }
        if (saleman.isMain()){
            holder.status.setText(R.string.manager);
            holder.salemanOperate.setVisibility(View.INVISIBLE);
            holder.updateSaleman.setVisibility(View.INVISIBLE);
        }else {
            holder.status.setText(R.string.saleman);
            holder.salemanOperate.setVisibility(View.VISIBLE);
            holder.updateSaleman.setVisibility(View.VISIBLE);
        }
        holder.name.setText(saleman.getName());
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null){
            switch (v.getId()){
                case R.id.saleman_operate:
                    mListener.onOperateClick((int) v.getTag(), false);
                    break;
                case R.id.update_saleman:
                    mListener.onOperateClick((int)v.getTag(), true);
                    break;
            }
        }
    }

    private OnOperateClickListener mListener;

    public void setOnOperateClickListener(OnOperateClickListener mListener){
        this.mListener = mListener;
    }

    public interface OnOperateClickListener{
        void onOperateClick(int pos, boolean isModify);
    }

    private class Holder{
        private Button salemanOperate;
        private Button updateSaleman;
        private TextView status;
        private TextView name;
    }
}
