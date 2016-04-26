package cn.com.incardata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.utils.DateCompute;

/**
 * Created by wanghao on 16/4/21.
 */
public class UnfinishListAdapter extends BaseAdapter implements View.OnClickListener{
    private Context context;
    private List<OrderInfo> orderList;
    private Map<Integer, String> orderType;

    public UnfinishListAdapter(Context context, List<OrderInfo> orderList) {
        this.context = context;
        this.orderList = orderList;
        orderType = MyApplication.skillMap;
    }

    @Override
    public int getCount() {
        return orderList == null ? 0 : orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.unfinish_order_list_item, parent, false);
            holder = new Holder();

            holder.orderNum = (TextView) convertView.findViewById(R.id.order_num);
            holder.orderStatus = (TextView) convertView.findViewById(R.id.order_status);
            holder.orderType = (TextView) convertView.findViewById(R.id.order_type);
            holder.orderTime = (TextView) convertView.findViewById(R.id.order_time);
            holder.orderOwner = (TextView) convertView.findViewById(R.id.order_owner);
            holder.orderPhoto = (ImageView) convertView.findViewById(R.id.order_image);
            holder.remark = (TextView) convertView.findViewById(R.id.remark);
            holder.createTime = (TextView) convertView.findViewById(R.id.create_time);

            holder.orderOwner.setOnClickListener(this);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        OrderInfo orderInfo = orderList.get(position);
        holder.orderNum.setText(context.getString(R.string.order_num, orderInfo.getOrderNum()));
        if (orderInfo.getMainTech() == null){
            holder.orderStatus.setText(R.string.no_receive);
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.gray_A3));
            holder.orderOwner.setVisibility(View.GONE);
        }else {
            holder.orderStatus.setText(R.string.received);
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.darkgray));
            holder.orderOwner.setText(orderInfo.getMainTech().getName());
            holder.orderOwner.setVisibility(View.VISIBLE);
        }
        holder.orderType.setText(orderType.get(orderInfo.getOrderType()));
        holder.orderTime.setText(context.getString(R.string.order_time, DateCompute.getDate(orderInfo.getOrderTime())));
        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + orderInfo.getPhoto(), holder.orderPhoto, 0);
        holder.remark.setText(context.getString(R.string.remark, orderInfo.getRemark()));
        holder.createTime.setText(context.getString(R.string.create_time, DateCompute.getDate(orderInfo.getAddTime())));

        holder.orderOwner.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (mListener!= null){
            mListener.onClickOwner((Integer) v.getTag());
        }
    }

    private OnClickOwnerListener mListener;

    public void setOnClickOwnerListener(OnClickOwnerListener mListener){
        this.mListener = mListener;
    }

    public interface OnClickOwnerListener{
       void onClickOwner(int position);
    }

    private class Holder{
        TextView orderNum;
        TextView orderStatus;
        TextView orderType;
        TextView orderTime;
        TextView orderOwner;
        ImageView orderPhoto;
        TextView remark;
        TextView createTime;
    }
}
