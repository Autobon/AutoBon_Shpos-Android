package cn.com.incardata.adapter;

import android.app.Activity;
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
 * 添加报价至套餐时套餐的列表适配
 * <p>Created by wangyang on 2019/9/25.</p>
 */
public class AddProductPopWindowAdapter extends BaseAdapter {
    private List<SetMealData> lists;
    private Activity context;

    public AddProductPopWindowAdapter(Activity context) {
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_add_product_pop_window, viewGroup, false);
            holder.textView = (TextView) view.findViewById(R.id.tv_set_meal_name);
            holder.btn_add = (Button) view.findViewById(R.id.btn_add);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        if (lists != null){
            holder.textView.setText(lists.get(position).getName());
        }

        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClickAdd(position);
                }
            }
        });
        return view;
    }


    class Holder {
        private TextView textView;
        private Button btn_add;
    }

    /**
     * 刷新数据
     * @param lists
     *
     */
    public void updata(List<SetMealData> lists) {
        this.lists = lists;
//        notifyDataSetChanged();
    }

    private AddOnClickListener mListener;

    public void setAddOnClickListener(AddOnClickListener mListener){
        this.mListener = mListener;
    }

    public interface AddOnClickListener{
        void onClickAdd(int position);
    }
}
