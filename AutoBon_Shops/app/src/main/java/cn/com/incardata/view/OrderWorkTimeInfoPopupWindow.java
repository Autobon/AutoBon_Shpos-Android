package cn.com.incardata.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.response.OrderWorkInfo;
import cn.com.incardata.utils.DateCompute;

/**
 * 订单施工时间详情弹框
 * <p>Created by wangyang on 2018/6/15.</p>
 */
public class OrderWorkTimeInfoPopupWindow extends PopupWindow {

    private ImageView img_close;                                                    //关闭图标
    private TextView[] tv_status = new TextView[6];                                 //状态
    private TextView[] tv_dates = new TextView[6];                                  //时间
    private View[] views = new View[5];                                             //中间的线
    private TextView[] tv_times = new TextView[5];                                  //所用时间
    private Activity context;

    public OrderWorkTimeInfoPopupWindow(Activity context) {
        this.context = context;
    }

    /**
     * 初始化
     */
    public void init(){
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_order_work_time_info,null);
        this.setContentView(view);

        img_close = (ImageView) view.findViewById(R.id.img_close);
        tv_status[0] = (TextView) view.findViewById(R.id.tv_status1);
        tv_status[1] = (TextView) view.findViewById(R.id.tv_status2);
        tv_status[2] = (TextView) view.findViewById(R.id.tv_status3);
        tv_status[3] = (TextView) view.findViewById(R.id.tv_status4);
        tv_status[4] = (TextView) view.findViewById(R.id.tv_status5);
        tv_status[5] = (TextView) view.findViewById(R.id.tv_status6);
        tv_dates[0] = (TextView) view.findViewById(R.id.tv_date1);
        tv_dates[1] = (TextView) view.findViewById(R.id.tv_date2);
        tv_dates[2] = (TextView) view.findViewById(R.id.tv_date3);
        tv_dates[3] = (TextView) view.findViewById(R.id.tv_date4);
        tv_dates[4] = (TextView) view.findViewById(R.id.tv_date5);
        tv_dates[5] = (TextView) view.findViewById(R.id.tv_date6);
        views[0] = view.findViewById(R.id.view1);
        views[1] = view.findViewById(R.id.view2);
        views[2] = view.findViewById(R.id.view3);
        views[3] = view.findViewById(R.id.view4);
        views[4] = view.findViewById(R.id.view5);
        tv_times[0] = (TextView) view.findViewById(R.id.tv_time1);
        tv_times[1] = (TextView) view.findViewById(R.id.tv_time2);
        tv_times[2] = (TextView) view.findViewById(R.id.tv_time3);
        tv_times[3] = (TextView) view.findViewById(R.id.tv_time4);
        tv_times[4] = (TextView) view.findViewById(R.id.tv_time5);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopupWindow();
            }
        });
        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                closePopupWindow();
            }
        });


        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth((int) (context.getWindowManager().getDefaultDisplay().getWidth() * 0.8));
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.SharePop);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    /**
     * 传递数据
     * @param lists
     */
    public void setData(List<OrderWorkInfo> lists){
        for (int i = 0; i < tv_status.length; i++) {
            if (i < lists.size()){
                tv_status[i].setTextColor(context.getResources().getColor(R.color.main_orange));
            }else {
                tv_status[i].setTextColor(context.getResources().getColor(R.color.darkgray));
            }
        }
        for (int i = 0; i < tv_status.length; i++) {
            if (i < lists.size()){
                tv_dates[i].setVisibility(View.VISIBLE);
                tv_dates[i].setText(DateCompute.getDate(lists.get(i).getRecordTime()));
            }else {
                tv_dates[i].setVisibility(View.GONE);
            }
        }
        for (int i = 0; i < views.length; i++) {
            if (i < lists.size() - 1){
                views[i].setBackgroundColor(context.getResources().getColor(R.color.main_orange));
            }else {
                views[i].setBackgroundColor(context.getResources().getColor(R.color.line_color));
            }
        }
        for (int i = 0; i < tv_times.length; i++) {
            if (i < lists.size() - 1){
                tv_times[i].setVisibility(View.VISIBLE);
            }else {
                tv_times[i].setVisibility(View.GONE);
            }
        }

        for (int i = 1; i < lists.size(); i++) {
            long time = lists.get(i).getRecordTime() - lists.get(i - 1).getRecordTime();
            int hour = (int) (time / (1000 * 3600));
            int minute = (int) ((time - hour * (1000 * 3600)) / (1000 * 60));
            int second = (int) ((time - hour * (1000 * 3600) - minute * (1000 * 60)) / 1000);
//            if (hour <= 0){
//                if (minute <= 0){
//                    if (second <= 0){
//                        tv_times[i].setText("用时1秒");
//                    }else {
//                        tv_times[i].setText("用时" + second + "秒");
//                    }
//                }else {
//                    tv_times[i].setText("用时" + minute + "分钟" + second + "秒");
//                }
//            }else {
//                tv_times[i].setText("用时" + hour + "小时" + minute + "分钟" + second + "秒");
//            }
            tv_times[i - 1].setText("用时" + hour + "时" + minute + "分" + second + "秒");
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        WindowManager.LayoutParams params=context.getWindow().getAttributes();
        params.alpha=0.7f;
        context.getWindow().setAttributes(params);
    }

    /**
     * 关闭窗口
     */
    public void closePopupWindow()
    {
        WindowManager.LayoutParams params = context.getWindow().getAttributes();
        params.alpha=1f;
        context.getWindow().setAttributes(params);
        dismiss();
    }
}
