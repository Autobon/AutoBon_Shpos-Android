package cn.com.incardata.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.autobon_shops.EnlargementActivity;
import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.utils.DateCompute;

/**未完成订单
 * Created by wanghao on 16/4/21.
 */
public class UnfinishListAdapter extends BaseAdapter{
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
            holder.appointTech = (Button) convertView.findViewById(R.id.appoint_tech);
            holder.revokeOrder = (Button) convertView.findViewById(R.id.revoke_order);
            holder.orderType = (TextView) convertView.findViewById(R.id.order_type);
            holder.orderTime = (TextView) convertView.findViewById(R.id.order_time);
            holder.orderOwner = (TextView) convertView.findViewById(R.id.order_owner);
            holder.orderPhoto = (ImageView) convertView.findViewById(R.id.order_image);
            holder.remark = (TextView) convertView.findViewById(R.id.remark);
            holder.createTime = (TextView) convertView.findViewById(R.id.create_time);

            holder.imageOnclick = new ImageOnclick(position);
            holder.appointTech.setOnClickListener(holder.imageOnclick);
            holder.revokeOrder.setOnClickListener(holder.imageOnclick);
            holder.orderPhoto.setOnClickListener(holder.imageOnclick);
            holder.orderOwner.setOnClickListener(holder.imageOnclick);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
            holder.imageOnclick.setPosition(position);
        }

        OrderInfo orderInfo = orderList.get(position);
        holder.orderNum.setText(context.getString(R.string.order_num, orderInfo.getOrderNum()));
        if ("CREATED_TO_APPOINT".equals(orderInfo.getStatus())){//新建订单，待指定技师
            holder.appointTech.setVisibility(View.VISIBLE);
            holder.orderOwner.setVisibility(View.GONE);
        }else {
            holder.appointTech.setVisibility(View.GONE);
            if (orderInfo.getMainTech() == null){
                holder.orderOwner.setVisibility(View.GONE);
            }else {
                holder.orderOwner.setText(orderInfo.getMainTech().getName());
                holder.orderOwner.setVisibility(View.VISIBLE);
            }
        }
        holder.orderType.setText(orderType.get(orderInfo.getOrderType()));
        holder.orderTime.setText(context.getString(R.string.order_time, DateCompute.getDate(orderInfo.getOrderTime())));
        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + orderInfo.getPhoto(), holder.orderPhoto, 0);
        holder.remark.setText(context.getString(R.string.remark, orderInfo.getRemark()));
        holder.createTime.setText(context.getString(R.string.create_time, DateCompute.getDate(orderInfo.getAddTime())));

        return convertView;
    }

    private OnClickOperateListener mListener;

    public void setonClickOperateListener(OnClickOperateListener mListener){
        this.mListener = mListener;
    }


    public interface OnClickOperateListener{
        /**
         *@param position 列表/视图位置（0开始）
         *@param type 1-指定技师，2-撤单,3-技师信息
         */
        void onClickOrderOperate(int position, int type);
    }

    private class Holder{
        TextView orderNum;
        Button appointTech;
        Button revokeOrder;
        TextView orderType;
        TextView orderTime;
        TextView orderOwner;
        ImageView orderPhoto;
        TextView remark;
        TextView createTime;
        ImageOnclick imageOnclick;
    }

    private class ImageOnclick extends AsInnerOnclick{
        public ImageOnclick(int position) {
            super(position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.appoint_tech:
                    if (mListener != null){
                        mListener.onClickOrderOperate(getPosition(), 1);
                    }
                    break;
                case R.id.revoke_order:
                    if (mListener != null){
                        mListener.onClickOrderOperate(getPosition(), 2);
                    }
                    break;
                case R.id.order_owner:
                    if (mListener != null){
                        mListener.onClickOrderOperate(getPosition(), 3);
                    }
                    break;
                case R.id.order_image:
                    Intent intent = new Intent(context, EnlargementActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("IMAGE_URL", new String[]{orderList.get(getPosition()).getPhoto()});
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    break;
            }
        }

    };

}
