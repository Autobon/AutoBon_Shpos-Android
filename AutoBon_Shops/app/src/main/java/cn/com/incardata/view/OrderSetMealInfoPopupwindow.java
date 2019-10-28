package cn.com.incardata.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
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

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.SetMealProductListAdapter;
import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.response.ProductData;

/**
 * 下单时查看报价详情的弹框
 * <p>Created by wangyang on 2019/10/8.</p>
 */
public class OrderSetMealInfoPopupwindow extends PopupWindow {
    private TextView set_meal_name;          //套餐名称
    private ListView mListView;              //列表控件
    private List<ProductData> mList;              //产品明细数据源
    private SetMealProductListAdapter mAdapter; //列表适配器
    private ImageView img_delete;           //关闭按钮
    private Activity context;

    public OrderSetMealInfoPopupwindow(Activity context) {
        this.context = context;
    }

    public void init() {

        View view = LayoutInflater.from(context).inflate(R.layout.pop_window_order_set_meal_info, null, false);
        this.setContentView(view);

        mList = new ArrayList<>();
        set_meal_name = (TextView) view.findViewById(R.id.set_meal_name);
        mListView = (ListView) view.findViewById(R.id.product_detailed_list);
        img_delete = (ImageView) view.findViewById(R.id.img_delete);

        mAdapter = new SetMealProductListAdapter(context, mList);
        mListView.setAdapter(mAdapter);

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

    public void setData(List<ProductData> lists, String content) {
        set_meal_name.setText(content);
        if (mList != null && mList.size() > 0){
            mList.clear();
        }
        mList.addAll(lists);
        mAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(mListView);
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


    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        DisplayMetrics outMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        int heightPixels = outMetrics.heightPixels;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        if (params.height > (int)(heightPixels * 0.7)) {
//            params.height = dip2px(context,340f);
            params.height = (int)(heightPixels * 0.7);
        }
        listView.setLayoutParams(params);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
