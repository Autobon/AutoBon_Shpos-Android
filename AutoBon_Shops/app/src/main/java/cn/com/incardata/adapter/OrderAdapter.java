package cn.com.incardata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.response.OrderInfo;

/**我的订单
 * Created by wanghao on 16/3/21.
 */
public class OrderAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private ArrayList<OrderInfo> mList;

    public OrderAdapter(Context context, ArrayList<OrderInfo> mList){
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList == null) return 0;
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.order_list_item, parent, false);
            holder = new Holder();

            holder.orderNum = (TextView) convertView.findViewById(R.id.order_number);
            holder.orderType = (TextView) convertView.findViewById(R.id.order_type);
            holder.orderOperate = (TextView) convertView.findViewById(R.id.order_operate);
            holder.orderImage = (ImageView) convertView.findViewById(R.id.order_image);

            holder.orderOperate.setOnClickListener(this);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        OrderInfo orderInfo = mList.get(position);
        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + orderInfo.getPhoto(), holder.orderImage, 0);
        holder.orderNum.setText(context.getString(R.string.order_num, orderInfo.getOrderNum()));
        holder.orderType.setText(MyApplication.getInstance().getSkill(orderInfo.getOrderType()));

        if ("EXPIRED".equals(orderInfo.getStatus())){//超时订单
            holder.orderOperate.setText(R.string.timeouted);
            holder.orderOperate.setTag(position);
        }else if (orderInfo.getComment() == null){//未评价
            holder.orderOperate.setText(R.string.uncomment);
            holder.orderOperate.setTag(position);
        }else {
            holder.orderOperate.setText(R.string.commented);
            holder.orderOperate.setTag(position);
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null){
            mListener.onClickOperate((Integer) v.getTag());
        }
    }

    private class Holder{
        TextView orderNum;
        TextView orderType;
        TextView orderOperate;
        ImageView orderImage;
    }

    private OnClickCommentListener mListener;

    public void setOnClickCommentListener(OnClickCommentListener mListener){
        this.mListener = mListener;
    }

    public interface OnClickCommentListener{
        void onClickOperate(int position);
    }

}
