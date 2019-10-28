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
import cn.com.incardata.http.response.ProductData;

/**
 * 套餐详情中的产品明细列表
 * <p>Created by wangyang on 2019/9/26.</p>
 */
public class SetMealProductListAdapter extends BaseAdapter {

    private Context context;
    private List<ProductData> mList;

    private boolean isUpdate = false;

    public SetMealProductListAdapter(Context context, List<ProductData> mList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_set_meal_peoduct_list, parent, false);
            holder = new Holder();
            holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            holder.tv_model = (TextView) convertView.findViewById(R.id.tv_model);
            holder.tv_brand = (TextView) convertView.findViewById(R.id.tv_brand);
            holder.tv_project = (TextView) convertView.findViewById(R.id.tv_project);
            holder.tv_position = (TextView) convertView.findViewById(R.id.tv_position);
            holder.tv_working_hours = (TextView) convertView.findViewById(R.id.tv_working_hours);
            holder.tv_quality_time = (TextView) convertView.findViewById(R.id.tv_quality_time);
            holder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(mList.get(position).getName())){
            holder.tv_project.setText(mList.get(position).getCode());
        }else {
            holder.tv_project.setText(context.getString(R.string.not));
        }

        if (!TextUtils.isEmpty(mList.get(position).getCode())){
            holder.tv_number.setText( mList.get(position).getCode());
        }else {
            holder.tv_number.setText( context.getString(R.string.not));
        }

        if (!TextUtils.isEmpty(mList.get(position).getModel())){
            holder.tv_model.setText( mList.get(position).getModel());
        }else {
            holder.tv_model.setText(context.getString(R.string.not));
        }

        if (!TextUtils.isEmpty(mList.get(position).getBrand())){
            holder.tv_brand.setText( mList.get(position).getBrand());
        }else {
            holder.tv_brand.setText( context.getString(R.string.not));
        }
        if (!TextUtils.isEmpty(mList.get(position).getConstructionPositionName())) {
            holder.tv_position.setText(mList.get(position).getConstructionPositionName());
        } else {
            holder.tv_position.setText("");
        }
        holder.tv_working_hours.setText(String.valueOf(mList.get(position).getWorkingHours()));
        holder.tv_quality_time.setText(mList.get(position).getWarranty() + "月");

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClickDelete(position);
                }
            }
        });

        if (isUpdate){
            holder.img_delete.setVisibility(View.VISIBLE);
        }else {
            holder.img_delete.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }


    private class Holder {
        TextView tv_number;             //编号
        TextView tv_model;              //型号
        TextView tv_brand;              //品牌
        TextView tv_project;            //施工项目
        TextView tv_position;           //施工部位
        TextView tv_working_hours;      //工时
        TextView tv_quality_time;       //质保期间
        ImageView img_delete;           //删除按钮
    }


    private DeleteOnClickListener mListener;

    public void setDeleteOnClickListener(DeleteOnClickListener mListener){
        this.mListener = mListener;
    }

    public interface DeleteOnClickListener{
        void onClickDelete(int position);
    }

    /**
     * 设置编辑
     */
    public void update(boolean isUpdates){
        isUpdate = isUpdates;
        notifyDataSetChanged();
    }
}
