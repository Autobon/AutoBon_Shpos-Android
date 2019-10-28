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
import cn.com.incardata.http.response.SetMealData;

/**
 * 下单时已经选择套餐的列表适配
 * <p>Created by wangyang on 2019/9/25.</p>
 */
public class OrderYetSelectSetMealListAdapter extends BaseAdapter {
    private List<SetMealData> lists;
    private Context context;

    public OrderYetSelectSetMealListAdapter(Context context, List<SetMealData> list) {
        this.context = context;
        this.lists = list;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_order_yet_select_set_meal_list, viewGroup, false);
            holder.textView = (TextView) view.findViewById(R.id.tv_set_meal_name);
            holder.btn_delete = (Button) view.findViewById(R.id.btn_delete);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        if (lists != null){
            holder.textView.setText(lists.get(position).getName());
        }

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClickDelete(position);
                }
            }
        });
        return view;
    }


    class Holder {
        private TextView textView;
        private Button btn_delete;
    }

    /**
     * 刷新数据
     * @param lists
     * @param
     */
    public void updata(List<SetMealData> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    private DeleteOnClickListener mListener;

    public void setDeleteOnClickListener(DeleteOnClickListener mListener){
        this.mListener = mListener;
    }

    public interface DeleteOnClickListener{
        void onClickDelete(int position);
    }
}
