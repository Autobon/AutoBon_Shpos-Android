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
import cn.com.incardata.utils.DateCompute;

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
            holder.orderWorkTime = (TextView) convertView.findViewById(R.id.order_workTime);

            holder.orderOperate.setOnClickListener(this);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        OrderInfo orderInfo = mList.get(position);
//        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + orderInfo.getPhoto(), holder.orderImage, 0);
        holder.orderNum.setText(context.getString(R.string.order_num, orderInfo.getOrderNum()));
        if (orderInfo.getType() == null){
            holder.orderType.setText("");
        }else {
            String[] types = orderInfo.getType().split(",");
            String type = "";
            for (int i = 0; i < types.length; i++) {
                type = type + getProjectName(types[i]) + ",";
            }
            if (type != "" && type.length() > 0){
                type = type.substring(0,type.length() - 1);
            }
            holder.orderType.setText(type);
        }
        holder.orderWorkTime.setText(context.getString(R.string.agreeTime) + DateCompute.getDate(orderInfo.getAgreedStartTime()));
        holder.orderOperate.setAlpha(1f);
        if ("EXPIRED".equals(orderInfo.getStatus())){//超时订单
            holder.orderOperate.setText(R.string.timeouted);
        }else if ("FINISHED".equals(orderInfo.getStatus())){//未评价
            holder.orderOperate.setText(R.string.uncomment);
        }else if ("COMMENTED".equals(orderInfo.getStatus())){
            holder.orderOperate.setText(R.string.commented);
        }else if ("GIVEN_UP".equals(orderInfo.getStatus()) || "CANCELED".equals(orderInfo.getStatus())){//撤单
            holder.orderOperate.setText(R.string.canceled_order);
            holder.orderOperate.setAlpha(0.4f);
        }
        holder.orderOperate.setTag(position);

        return convertView;
    }


    @Override
    public void onClick(View v) {
        if (mListener != null){
            mListener.onClickOperate((Integer) v.getTag());
        }
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

    private class Holder{
        TextView orderNum;
        TextView orderType;
        TextView orderOperate;
        TextView orderWorkTime;
    }

    private OnClickCommentListener mListener;

    public void setOnClickCommentListener(OnClickCommentListener mListener){
        this.mListener = mListener;
    }

    public interface OnClickCommentListener{
        void onClickOperate(int position);
    }

}
