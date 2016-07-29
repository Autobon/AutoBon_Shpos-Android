package cn.com.incardata.adapter;

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
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AppointTechEntity;
import cn.com.incardata.http.response.SearchTech_Data_Item;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.CircleImageView;

/**
 * 指定技师
 * @author wanghao
 */
public class MyContactAdapter extends BaseAdapter{
    private BaseActivity activity;
    private List<SearchTech_Data_Item> mList;
    private String technicianName;
    private int technicianId;

    public MyContactAdapter(BaseActivity activity, List<SearchTech_Data_Item> mList){
        this.activity = activity;
        this.mList = mList;
    }

    @Override
    public int getCount() {
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.add_contact_item, parent, false);
            holder.circleImageView = (CircleImageView)convertView.findViewById(R.id.iv_circle);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.btn_submit = (Button) convertView.findViewById(R.id.btn_submit);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        final SearchTech_Data_Item data = mList.get(position);
        if(data.getAvatar()!=null){
            String imageUrl = NetURL.IP_PORT+data.getAvatar();
            Log.i("test","imageUrl=======>"+imageUrl);
            ImageLoaderCache.getInstance().loader(imageUrl,holder.circleImageView);
        }
        holder.tv_username.setText(data.getName());
        holder.tv_phone.setText(data.getPhone());

        holder.btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                technicianName = data.getName();
                technicianId = data.getId();
                addTechnician(); //添加技师
            }
        });

        return convertView;
    }

    public void addTechnician(){
        activity.showDialog(null);
        //TODO 发送合作邀请
        Http.getInstance().postTaskToken(NetURL.APPOINT, AppointTechEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                activity.cancelDialog();
                if(entity == null){
                    T.show(activity,activity.getString(R.string.operate_failed_agen));
                    return;
                }
                AppointTechEntity tech = (AppointTechEntity) entity;
                if(tech.isResult()){
                    Log.i("test","指定技师成功");
                    T.show(activity, R.string.appoint_technician_succeed);
//                    Intent i=new Intent();
//                    i.putExtra("username",technicianName);
//                    i.putExtra("technicianId",technicianId);
//                    activity.setResult(activity.RESULT_OK,i);
                    activity.finish();
                }else{
                    T.show(activity,tech.getMessage());
                }
            }
        }, new BasicNameValuePair("orderId", String.valueOf(orderId)), new BasicNameValuePair("techId", String.valueOf(technicianId)));
    }

    static class ViewHolder{
        CircleImageView circleImageView;
        TextView tv_username;
        TextView tv_phone;
        Button btn_submit;
    }

    private int orderId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
