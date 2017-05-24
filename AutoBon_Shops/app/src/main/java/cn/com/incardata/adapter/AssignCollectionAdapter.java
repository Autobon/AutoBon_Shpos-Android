package cn.com.incardata.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.response.CollectionTechnician_Data;

/**
 * 指派收藏技师适配
 * Created by yang on 2017/5/24.
 */
public class AssignCollectionAdapter extends BaseAdapter {
    private Activity activity;
    private List<CollectionTechnician_Data> list;

    public AssignCollectionAdapter(List<CollectionTechnician_Data> list, Activity activity) {
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.assign_collection_technician_list_item, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
//            holder.orderNum = (TextView) convertView.findViewById(R.id.orderNum);
            holder.assign = (Button) convertView.findViewById(R.id.assign);
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



        holder.assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onClick(position);
                }
            }
        });

        return convertView;
    }


    static class ViewHolder {
        TextView name;
        //        TextView orderNum;
        TextView filmLevelNum;
        TextView carCoverLevelNum;
        TextView colorModifyLevelNum;
        TextView beautyLevelNum;
        //        TextView tv_distance;
        Button assign;
    }

    private ClickListener listener;


    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public interface ClickListener {
        void onClick(int position);
    }
}
