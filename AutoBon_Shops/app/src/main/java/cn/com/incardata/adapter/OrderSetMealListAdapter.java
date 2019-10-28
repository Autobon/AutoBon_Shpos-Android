package cn.com.incardata.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.response.ProductData;
import cn.com.incardata.http.response.SetMealData;

/**
 * 下单时选择套餐的列表适配
 * <p>Created by wangyang on 2019/9/25.</p>
 */
public class OrderSetMealListAdapter extends BaseAdapter {
    private List<SetMealData> lists;
    private Context context;

    private List<SetMealData> selecdedList;     //已选套餐产品

    public OrderSetMealListAdapter(Context context,List<SetMealData> list) {
        this.context = context;
        this.lists = list;
        selecdedList = new ArrayList<>();
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
            view = LayoutInflater.from(context).inflate(R.layout.item_order_set_meal_list, viewGroup, false);
            holder.textView = (TextView) view.findViewById(R.id.tv_set_meal_name);
            holder.btn_select = (Button) view.findViewById(R.id.btn_select);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        if (lists != null){
            holder.textView.setText(lists.get(position).getName());
        }

        boolean isSelect = false;
        if (selecdedList != null) {
            if (selecdedList.size() > 0) {
                for (SetMealData data : selecdedList) {
                    if (data.getId() == lists.get(position).getId()){
                        isSelect = true;
                        break;
                    }
                }
            } else {
                isSelect = false;
            }

        }


        if (isSelect) {
            holder.btn_select.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.yet_select_btn_style));
            holder.btn_select.setTextColor(context.getResources().getColor(R.color.text_black));
            holder.btn_select.setText(context.getString(R.string.yet_select));
        } else {
            holder.btn_select.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.add_btn_style));
            holder.btn_select.setTextColor(context.getResources().getColor(R.color.tv_white));
            holder.btn_select.setText(context.getString(R.string.select));
        }

        final boolean finalIsSelect = isSelect;
        holder.btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if (finalIsSelect) {
                        mListener.onClickDelete(lists.get(position));
                    } else {
                        mListener.onClickSelect(lists.get(position));
                    }
                }
            }
        });

        return view;
    }


    class Holder {
        private TextView textView;
        private Button btn_select;
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


    /**
     * 设置已选择数据
     */
    public void setData(List<SetMealData> datas) {
        if (selecdedList != null && selecdedList.size() > 0) {
            selecdedList.clear();
        }
        selecdedList.addAll(datas);
        notifyDataSetChanged();
    }

    private SelectOnClickListener mListener;

    public void setSelectOnClickListener(SelectOnClickListener mListener){
        this.mListener = mListener;
    }

    public interface SelectOnClickListener{
        void onClickSelect(SetMealData data);
        void onClickDelete(SetMealData data);
    }
}
