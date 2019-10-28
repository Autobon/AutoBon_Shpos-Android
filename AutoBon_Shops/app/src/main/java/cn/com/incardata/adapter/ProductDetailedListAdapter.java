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
 * 产品明细列表
 * <p>Created by wangyang on 2019/9/25.</p>
 */
public class ProductDetailedListAdapter extends BaseAdapter {

    private Context context;
    private List<ProductData> mList;

    public ProductDetailedListAdapter(Context context, List<ProductData> mList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product_detailed_list, parent, false);
            holder = new Holder();
            holder.view = convertView.findViewById(R.id.view);
            holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            holder.tv_model = (TextView) convertView.findViewById(R.id.tv_model);
            holder.tv_brand = (TextView) convertView.findViewById(R.id.tv_brand);
            holder.tv_project = (TextView) convertView.findViewById(R.id.tv_project);
            holder.tv_position = (TextView) convertView.findViewById(R.id.tv_position);
            holder.tv_working_hours = (TextView) convertView.findViewById(R.id.tv_working_hours);
            holder.tv_quality_time = (TextView) convertView.findViewById(R.id.tv_quality_time);
            holder.ll_visable = (LinearLayout) convertView.findViewById(R.id.ll_visable);
            holder.ll_visable_click = (LinearLayout) convertView.findViewById(R.id.ll_visable_click);
            holder.tv_visable = (TextView) convertView.findViewById(R.id.tv_visable);
            holder.img_visable = (ImageView) convertView.findViewById(R.id.img_visable);
            holder.tv_add_set_meal = (TextView) convertView.findViewById(R.id.tv_add_set_meal);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (position == 0) {
            holder.view.setVisibility(View.GONE);
        } else {
            holder.view.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(mList.get(position).getCode())) {
            holder.tv_number.setText(context.getString(R.string.code_1_s, mList.get(position).getCode()));
        } else {
            holder.tv_number.setText(context.getString(R.string.code_1_s, context.getString(R.string.not)));
        }

        if (!TextUtils.isEmpty(mList.get(position).getModel())) {
            holder.tv_model.setText(context.getString(R.string.model_1_s, mList.get(position).getModel()));
        } else {
            holder.tv_model.setText(context.getString(R.string.model_1_s, context.getString(R.string.not)));
        }

        if (!TextUtils.isEmpty(mList.get(position).getBrand())) {
            holder.tv_brand.setText(context.getString(R.string.brand_1_s, mList.get(position).getBrand()));
        } else {
            holder.tv_brand.setText(context.getString(R.string.brand_1_s, context.getString(R.string.not)));
        }
        if (!TextUtils.isEmpty(mList.get(position).getConstructionPositionName())) {
            holder.tv_position.setText(context.getString(R.string.position_1_s, mList.get(position).getConstructionPositionName()));
        } else {
            holder.tv_position.setText(context.getString(R.string.position_1_s, context.getString(R.string.not)));
        }
        holder.tv_project.setText(context.getString(R.string.project_1_s, getProjectName(mList.get(position).getType())));

        holder.tv_working_hours.setText(context.getString(R.string.working_hours_1_s, String.valueOf(mList.get(position).getWorkingHours())));
        holder.tv_quality_time.setText(context.getString(R.string.quality_time_1_s, mList.get(position).getWarranty() + "月"));


        Holder finalHolder = holder;
        holder.ll_visable_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.get(position).setVisible(!mList.get(position).isVisible());
                notifyDataSetChanged();
                //                if (finalHolder.ll_visable.getVisibility() == View.VISIBLE) {
//                    finalHolder.ll_visable.setVisibility(View.GONE);
//                    finalHolder.img_visable.setImageDrawable(context.getResources().getDrawable(R.mipmap.sjxd_list_more));
//
//                }else {
//                    finalHolder.ll_visable.setVisibility(View.VISIBLE);
//                    finalHolder.img_visable.setImageDrawable(context.getResources().getDrawable(R.mipmap.tc_gbxq_btn));
//                }
            }
        });


        if (mList.get(position).isVisible()) {
            holder.ll_visable.setVisibility(View.VISIBLE);
            holder.img_visable.setImageDrawable(context.getResources().getDrawable(R.mipmap.tc_gbxq_btn));
        } else {
            holder.ll_visable.setVisibility(View.GONE);
            holder.img_visable.setImageDrawable(context.getResources().getDrawable(R.mipmap.sjxd_list_more));
        }

        holder.tv_add_set_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickAdd(position);
                }
            }
        });


        return convertView;
    }


    private class Holder {
        View view;                      //线
        TextView tv_number;             //编号
        TextView tv_model;              //型号
        TextView tv_brand;              //品牌
        TextView tv_project;            //施工项目
        TextView tv_position;           //施工部位
        TextView tv_working_hours;      //工时
        TextView tv_quality_time;       //质保期间
        LinearLayout ll_visable;        //隐藏部分
        LinearLayout ll_visable_click;  //点击切换隐藏
        TextView tv_visable;            //隐藏文本
        ImageView img_visable;          //隐藏图片
        TextView tv_add_set_meal;       //添加至套餐
        boolean isVisible = false;
    }


    /**
     * 施工项目对应的值
     *
     * @param type
     * @return
     */
    public String getProjectName(int type) {

        if (1 == type) {
            return "隔热膜";
        } else if (2 == type) {
            return "隐形车衣";
        } else if (3 == type) {
            return "车身改色";
        } else if (4 == type) {
            return "美容清洁";
        } else {
            return context.getString(R.string.not);
        }

    }


    private AddOnClickListener mListener;

    public void setAddOnClickListener(AddOnClickListener mListener) {
        this.mListener = mListener;
    }

    public interface AddOnClickListener {
        void onClickAdd(int position);
    }
}
