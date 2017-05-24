package cn.com.incardata.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;

import cn.com.incardata.autobon_shops.BaseActivity;
import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AppointTechEntity;
import cn.com.incardata.http.response.CollectionTechnician;
import cn.com.incardata.http.response.CollectionTechnician_Data;
import cn.com.incardata.utils.T;

/**
 * 已收藏的技师列表适配
 * Created by yang on 2017/5/23.
 */
public class CollectionTechnicianAdapter extends BaseAdapter {
    private Activity activity;
    private List<CollectionTechnician_Data> list;

    public CollectionTechnicianAdapter(List<CollectionTechnician_Data> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.collection_technician_list_item, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
//            holder.orderNum = (TextView) convertView.findViewById(R.id.orderNum);
            holder.delect = (Button) convertView.findViewById(R.id.delete);
            holder.filmLevelNum = (TextView) convertView.findViewById(R.id.filmLevelNum);
            holder.carCoverLevelNum = (TextView) convertView.findViewById(R.id.carCoverLevelNum);
            holder.colorModifyLevelNum = (TextView) convertView.findViewById(R.id.colorModifyLevelNum);
            holder.beautyLevelNum = (TextView) convertView.findViewById(R.id.beautyLevelNum);
//            holder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CollectionTechnician_Data technicianMessage = list.get(position);
        holder.name.setText(technicianMessage.getTechnician().getName());

//        if (technicianMessage.getOrderCount() == null){
//            holder.orderNum.setText("0单");
//        }else {
//            holder.orderNum.setText(technicianMessage.getOrderCount() + "单");
//        }

//        if (technicianMessage.getDistance() == null){
//            holder.tv_distance.setText("0.00km");
//        }else {
//            holder.tv_distance.setText(DecimalUtil.FloatDecimal2(technicianMessage.getDistance()) + "KM");
//        }
        holder.filmLevelNum.setText(technicianMessage.getTechnician().getFilmLevel() + "星");
        holder.carCoverLevelNum.setText(technicianMessage.getTechnician().getCarCoverLevel() + "星");
        holder.colorModifyLevelNum.setText(technicianMessage.getTechnician().getColorModifyLevel() + "星");
        holder.beautyLevelNum.setText(technicianMessage.getTechnician().getBeautyLevel() + "星");



        holder.delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onClick(position);
                }
            }
        });

        return convertView;
    }

//    public void addTechnician(){
//        activity.showDialog(null);
//        //TODO 发送合作邀请
//        Http.getInstance().postTaskToken(NetURL.APPOINTV2, AppointTechEntity.class, new OnResult() {
//            @Override
//            public void onResult(Object entity) {
//                activity.cancelDialog();
//                if(entity == null){
//                    T.show(activity,activity.getString(R.string.operate_failed_agen));
//                    return;
//                }
//                AppointTechEntity tech = (AppointTechEntity) entity;
//                if(tech.isStatus()){
//                    Log.i("test","指定技师成功");
//                    T.show(activity, R.string.appoint_technician_succeed);
//                    Intent i=new Intent();
//                    i.putExtra("username",technicianName);
//                    i.putExtra("technicianId",technicianId);
//                    activity.setResult(activity.RESULT_OK,i);
//                    activity.finish();
//                }else{
//                    T.show(activity,tech.getMessage());
//                }
//            }
//        }, new BasicNameValuePair("orderId", String.valueOf(orderId)), new BasicNameValuePair("techId", String.valueOf(technicianId)));
//    }

    static class ViewHolder {
        TextView name;
        //        TextView orderNum;
        TextView filmLevelNum;
        TextView carCoverLevelNum;
        TextView colorModifyLevelNum;
        TextView beautyLevelNum;
        //        TextView tv_distance;
        Button delect;
    }

    private ClickListener listener;


    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public interface ClickListener {
        void onClick(int position);
    }
}
