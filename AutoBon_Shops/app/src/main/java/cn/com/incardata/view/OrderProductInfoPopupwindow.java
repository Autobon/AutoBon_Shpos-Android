package cn.com.incardata.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.adapter.AddProductPopWindowAdapter;
import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.response.ProductData;
import cn.com.incardata.http.response.SetMealData;

/**
 * 下单时查看报价详情的弹框
 * <p>Created by wangyang on 2019/10/8.</p>
 */
public class OrderProductInfoPopupwindow extends PopupWindow {
    private TextView tv_number;             //编号
    private TextView tv_model;              //型号
    private TextView tv_brand;              //品牌
    private TextView tv_project;            //施工项目
    private TextView tv_position;           //施工部位
    private TextView tv_working_hours;      //工时
    private TextView tv_quality_time;       //质保期间
    private ImageView img_delete;           //删除按钮
    private Activity context;

    public OrderProductInfoPopupwindow(Activity context) {
        this.context = context;
    }

    public void init() {

        View view = LayoutInflater.from(context).inflate(R.layout.pop_window_order_product_info, null, false);
        this.setContentView(view);
        tv_number = (TextView) view.findViewById(R.id.tv_number);
        tv_model = (TextView) view.findViewById(R.id.tv_model);
        tv_brand = (TextView) view.findViewById(R.id.tv_brand);
        tv_project = (TextView) view.findViewById(R.id.tv_project);
        tv_position = (TextView) view.findViewById(R.id.tv_position);
        tv_working_hours = (TextView) view.findViewById(R.id.tv_working_hours);
        tv_quality_time = (TextView) view.findViewById(R.id.tv_quality_time);
        img_delete = (ImageView) view.findViewById(R.id.img_delete);

        img_delete.setOnClickListener(new View.OnClickListener() {
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
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
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
     * 赋值
     * @param data
     */
    public void setData(ProductData data) {
        if (!TextUtils.isEmpty(data.getName())){
           tv_project.setText(data.getCode());
        }else {
           tv_project.setText(context.getString(R.string.not));
        }

        if (!TextUtils.isEmpty(data.getCode())){
           tv_number.setText( data.getCode());
        }else {
           tv_number.setText( context.getString(R.string.not));
        }

        if (!TextUtils.isEmpty(data.getModel())){
           tv_model.setText( data.getModel());
        }else {
           tv_model.setText(context.getString(R.string.not));
        }

        if (!TextUtils.isEmpty(data.getBrand())){
           tv_brand.setText( data.getBrand());
        }else {
           tv_brand.setText( context.getString(R.string.not));
        }
       tv_position.setText(String.valueOf(data.getConstructionPosition()));
       tv_working_hours.setText(String.valueOf(data.getWorkingHours()));
       tv_quality_time.setText(data.getWarranty() + "月");
    }


    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        WindowManager.LayoutParams params = context.getWindow().getAttributes();
        params.alpha = 0.5f;
        context.getWindow().setAttributes(params);
    }


    /**
     * 关闭窗口
     */
    public void closePopupWindow() {
        WindowManager.LayoutParams params = context.getWindow().getAttributes();
        params.alpha = 1f;
        context.getWindow().setAttributes(params);
        dismiss();
    }


}
