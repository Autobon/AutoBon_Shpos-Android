package cn.com.incardata.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.autobon_shops.R;


/**
 * 订单状态列表适配
 * <p>Created by wangyang on 2018/6/14.</p>
 */
public class SelectPopupListViewAdapter extends BaseAdapter {
    private List<String> lists;
    private Activity context;
    private int checkId = -1;

    //    public SelectPopupListViewAdapter(List<String> lists, Activity context, int checkId) {
//        this.lists = lists;
//        this.context = context;
//        this.checkId = checkId;
//    }
    public SelectPopupListViewAdapter(Activity context) {
//        this.lists = lists;
        this.context = context;

    }

    @Override
    public int getCount() {
        if (lists != null)
            return lists.size();
        return 0;


    }

    @Override
    public Object getItem(int position) {
        if (lists != null)
            return lists.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.status_list_item, viewGroup, false);
            holder.textView = (TextView) view.findViewById(R.id.text_value);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        if (lists != null){
            holder.textView.setText(lists.get(position));
            if (position == checkId) {
                holder.textView.setTextColor(context.getResources().getColor(R.color.main_orange));
            }else {
                holder.textView.setTextColor(context.getResources().getColor(R.color.darkgray));
            }
        }
        return view;
    }


    class Holder {
        private TextView textView;
    }


    public void updata(List<String> lists, int checkId) {
        this.lists = lists;
        this.checkId = checkId;
        notifyDataSetChanged();
    }
}
