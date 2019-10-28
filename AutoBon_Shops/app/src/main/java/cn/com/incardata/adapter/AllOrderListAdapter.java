package cn.com.incardata.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.utils.DateCompute;

/**
 * 所有订单列表适配
 * <p>Created by wangyang on 2018/6/14.</p>
 */
public class AllOrderListAdapter extends BaseAdapter {
    private Context context;
    private List<OrderInfo> mList;

    public AllOrderListAdapter(Context context,List<OrderInfo> mList) {
        this.mList = mList;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_all_order_list, parent, false);
            holder = new Holder();

            holder.orderNum = (TextView) convertView.findViewById(R.id.order_num);
            holder.tv_license = (TextView) convertView.findViewById(R.id.tv_license);
            holder.tv_vin = (TextView) convertView.findViewById(R.id.tv_vin);
            holder.appointTech = (Button) convertView.findViewById(R.id.appoint_tech);
            holder.revokeOrder = (Button) convertView.findViewById(R.id.revoke_order);
            holder.orderOperate = (Button) convertView.findViewById(R.id.orderOperate);
            holder.orderType = (TextView) convertView.findViewById(R.id.orderType);
            holder.orderStatus = (TextView) convertView.findViewById(R.id.orderStatus);
            holder.orderWorkTime = (TextView) convertView.findViewById(R.id.orderWorkTime);
            holder.orderContract = (TextView) convertView.findViewById(R.id.orderContract);

            holder.imageOnclick = new ImageOnclick(position);
            holder.appointTech.setOnClickListener(holder.imageOnclick);
            holder.revokeOrder.setOnClickListener(holder.imageOnclick);
            holder.orderOperate.setOnClickListener(holder.imageOnclick);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
            holder.imageOnclick.setPosition(position);
        }

        OrderInfo orderInfo = mList.get(position);
        holder.orderNum.setText(context.getString(R.string.order_num, orderInfo.getOrderNum()));
        if (!TextUtils.isEmpty(orderInfo.getLicense())){
            holder.tv_license.setText(context.getString(R.string.license_num, orderInfo.getLicense()));
        }else {
            holder.tv_license.setText(context.getString(R.string.license_num, context.getString(R.string.not)));
        }
        if (!TextUtils.isEmpty(orderInfo.getVin())){
            holder.tv_vin.setText(context.getString(R.string.vin_num, orderInfo.getVin()));
        }else {
            holder.tv_vin.setText(context.getString(R.string.vin_num, context.getString(R.string.not)));
        }
        if ("CREATED_TO_APPOINT".equals(orderInfo.getStatus())) {//新建订单，待指定技师
            holder.appointTech.setVisibility(View.VISIBLE);
            holder.revokeOrder.setVisibility(View.VISIBLE);
            holder.orderOperate.setVisibility(View.GONE);
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.gray_A3));
            holder.orderStatus.setText("待指派");
        } else {
            holder.appointTech.setVisibility(View.GONE);
            if ("NEWLY_CREATED".equals(orderInfo.getStatus())) {
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.gray_A3));
                holder.orderStatus.setText("未接单");
                holder.revokeOrder.setVisibility(View.VISIBLE);
                holder.orderOperate.setVisibility(View.GONE);
            } else if ("TAKEN_UP".equals(orderInfo.getStatus())) {
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.darkgray));
                holder.orderStatus.setText("已接单");
                holder.revokeOrder.setVisibility(View.VISIBLE);
                holder.orderOperate.setVisibility(View.GONE);
            } else if ("IN_PROGRESS".equals(orderInfo.getStatus())) {
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.main_orange));
                holder.orderStatus.setText("已出发");
                holder.revokeOrder.setVisibility(View.VISIBLE);
                holder.orderOperate.setVisibility(View.GONE);
            } else if ("SIGNED_IN".equals(orderInfo.getStatus())) {
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.main_orange));
                holder.orderStatus.setText("已签到");
                holder.revokeOrder.setVisibility(View.VISIBLE);
                holder.orderOperate.setVisibility(View.GONE);
            } else if ("AT_WORK".equals(orderInfo.getStatus())) {
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.main_orange));
                holder.orderStatus.setText("施工中");
                holder.revokeOrder.setVisibility(View.VISIBLE);
                holder.orderOperate.setVisibility(View.GONE);
            }else if ("EXPIRED".equals(orderInfo.getStatus())){//超时订单
                holder.orderStatus.setText(R.string.timeouted);
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.gray_A3));
                holder.revokeOrder.setVisibility(View.GONE);
                holder.orderOperate.setVisibility(View.GONE);
            }else if ("FINISHED".equals(orderInfo.getStatus())){//未评价
                holder.orderStatus.setText(R.string.uncomment1);
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.main_orange));
                holder.revokeOrder.setVisibility(View.GONE);
                holder.orderOperate.setVisibility(View.VISIBLE);
            }else if ("COMMENTED".equals(orderInfo.getStatus())){
                holder.orderStatus.setText(R.string.commented);
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.main_orange));
                holder.revokeOrder.setVisibility(View.GONE);
                holder.orderOperate.setVisibility(View.GONE);
            }else if ("GIVEN_UP".equals(orderInfo.getStatus()) || "CANCELED".equals(orderInfo.getStatus())){//撤单
                holder.orderStatus.setText(R.string.canceled_order);
                holder.orderStatus.setTextColor(context.getResources().getColor(R.color.gray_A3));
                holder.revokeOrder.setVisibility(View.GONE);
                holder.orderOperate.setVisibility(View.GONE);
            }
        }

        holder.orderWorkTime.setText(context.getString(R.string.order_time1, DateCompute.getDate(orderInfo.getAgreedStartTime())));

        holder.orderContract.setText(context.getString(R.string.order_contract,orderInfo.getContactPhone()));
        if (orderInfo.getType() == null) {
            holder.orderType.setText("");
        } else {
            String[] types = orderInfo.getType().split(",");
            String type = "";
            for (int i = 0; i < types.length; i++) {
                type = type + getProjectName(types[i]) + ",";
            }
            if (type != "" && type.length() > 0) {
                type = type.substring(0, type.length() - 1);
            }
            holder.orderType.setText(type);
        }

//        holder.orderType.setText(orderType.get(orderInfo.getOrderType()));
//        holder.orderTime.setText(context.getString(R.string.order_time, DateCompute.getDate(orderInfo.getOrderTime())));
//        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + orderInfo.getPhoto(), holder.orderPhoto, 0);
//        holder.remark.setText(context.getString(R.string.remark, orderInfo.getRemark()));
//        holder.createTime.setText(context.getString(R.string.create_time, DateCompute.getDate(orderInfo.getAddTime())));

        return convertView;
    }

    private class Holder {
        TextView orderNum;
        TextView tv_license;
        TextView tv_vin;
        Button appointTech;
        Button revokeOrder;
        Button orderOperate;
        TextView orderType;
        TextView orderStatus;
        TextView orderWorkTime;
        TextView orderContract;
        ImageOnclick imageOnclick;
    }

    private OnClickOperateListener mListener;

    public void setonClickOperateListener(OnClickOperateListener mListener) {
        this.mListener = mListener;
    }


    public interface OnClickOperateListener {
        /**
         * @param position 列表/视图位置（0开始）
         * @param type     1-指定技师，2-撤单,3-技师信息
         */
        void onClickOrderOperate(int position, int type);
    }

    public String getProjectName(String type) {
        if ("1".equals(type)) {
            return "隔热膜";
        } else if ("2".equals(type)) {
            return "隐形车衣";
        } else if ("3".equals(type)) {
            return "车身改色";
        } else if ("4".equals(type)) {
            return "美容清洁";
        }
        if (type == null) ;
        return null;
    }

    private class ImageOnclick extends AsInnerOnclick {
        public ImageOnclick(int position) {
            super(position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.appoint_tech:
                    if (mListener != null) {
                        mListener.onClickOrderOperate(getPosition(), 1);
                    }
                    break;
                case R.id.revoke_order:
                    if (mListener != null) {
                        mListener.onClickOrderOperate(getPosition(), 2);
                    }
                    break;
                case R.id.orderOperate:
                    if (mListener != null) {
                        mListener.onClickOrderOperate(getPosition(), 3);
                    }
                    break;
            }
        }

    }
}
