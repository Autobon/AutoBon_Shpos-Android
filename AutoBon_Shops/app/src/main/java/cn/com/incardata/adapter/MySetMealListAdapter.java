package cn.com.incardata.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.response.SetMealData;

/**
 * 我的套餐列表适配
 * <p>Created by wangyang on 2019/9/26.</p>
 */
public class MySetMealListAdapter extends BaseAdapter {

    private Context context;
    private List<SetMealData> mList;

    public MySetMealListAdapter(Context context, List<SetMealData> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_set_meal_list, parent, false);
            holder = new Holder();
            holder.view = convertView.findViewById(R.id.view);
            holder.tv_set_meal_name = (TextView) convertView.findViewById(R.id.tv_set_meal_name);
            holder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (position == 0){
            holder.view.setVisibility(View.GONE);
        }else {
            holder.view.setVisibility(View.VISIBLE);
        }
        if (mList != null){
            if (!TextUtils.isEmpty(mList.get(position).getName())){
                holder.tv_set_meal_name.setText(mList.get(position).getName());
            }else {
                holder.tv_set_meal_name.setText(R.string.not);
            }
        }


        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClickDelete(position);
                }
            }
        });


        return convertView;
    }


    private class Holder {
        View view;                             //线
        TextView tv_set_meal_name;             //套餐名称
        ImageView img_delete;                  //删除套餐按钮
    }


    private DeleteOnClickListener mListener;

    public void setDeleteOnClickListener(MySetMealListAdapter.DeleteOnClickListener mListener){
        this.mListener = mListener;
    }

    public interface DeleteOnClickListener{
        void onClickDelete(int position);
    }
}
