package cn.com.incardata.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.response.ProductData;

/**
 * 数据下单时产品报价列表适配器
 * <p>Created by wangyang on 2019/10/8.</p>
 */
public class OrderProductListAdapter extends BaseAdapter {
    private Context context;
    private List<ProductData> mList;

    private List<ProductData> selecdedList;     //已选报价产品

    private boolean isUpdate = false;

    public OrderProductListAdapter(Context context, List<ProductData> mList) {
        this.context = context;
        this.mList = mList;
        selecdedList = new ArrayList<>();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_peoduct_list, parent, false);
            holder = new Holder();
            holder.btn_select = (Button) convertView.findViewById(R.id.btn_select);
            holder.tv_model = (TextView) convertView.findViewById(R.id.tv_model);
            holder.tv_project = (TextView) convertView.findViewById(R.id.tv_project);
            holder.tv_position = (TextView) convertView.findViewById(R.id.tv_position);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        boolean isSelect = false;
        if (selecdedList != null) {
            if (selecdedList.size() > 0) {
                for (ProductData data : selecdedList) {
                    if (data.getId() == mList.get(position).getId()){
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


        if (!TextUtils.isEmpty(mList.get(position).getCode())) {
            holder.tv_project.setText(mList.get(position).getCode());
        } else {
            holder.tv_project.setText(context.getString(R.string.not));
        }

        if (!TextUtils.isEmpty(mList.get(position).getModel())) {
            holder.tv_model.setText(mList.get(position).getModel());
        } else {
            holder.tv_model.setText("");
        }

        if (!TextUtils.isEmpty(mList.get(position).getConstructionPositionName())) {
            holder.tv_position.setText(mList.get(position).getConstructionPositionName());
        } else {
            holder.tv_position.setText("");
        }


        final boolean finalIsSelect = isSelect;
        holder.btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if (finalIsSelect) {
                        mListener.onClickDelete(mList.get(position));
                    } else {
                        mListener.onClickSelect(mList.get(position));
                    }
                }
            }
        });


        return convertView;
    }


    private class Holder {
        Button btn_select;              //选择按钮
        TextView tv_model;              //型号
        TextView tv_project;            //施工项目
        TextView tv_position;           //施工部位

    }


    private SelectOnClickListener mListener;

    public void setSelectOnClickListener(SelectOnClickListener mListener) {
        this.mListener = mListener;
    }

    public interface SelectOnClickListener {
        void onClickSelect(ProductData data);

        void onClickDelete(ProductData data);
    }

    /**
     * 设置已选择数据
     */
    public void setData(List<ProductData> datas) {
        if (mList != null && mList.size() > 0) {
            mList.clear();
        }
        mList.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 设置已选择数据
     */
    public void update(List<ProductData> selectList) {
        if (selecdedList != null && selecdedList.size() > 0) {
            selecdedList.clear();
        }
        selecdedList.addAll(selectList);
        notifyDataSetChanged();
    }
}
