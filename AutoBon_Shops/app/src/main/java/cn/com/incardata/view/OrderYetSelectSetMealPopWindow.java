package cn.com.incardata.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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

import cn.com.incardata.adapter.AddProductPopWindowAdapter;
import cn.com.incardata.adapter.OrderYetSelectSetMealListAdapter;
import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.http.response.SetMealData;

/**
 * 下单时查看已选套餐的弹出框
 * <p>Created by wangyang on 2019/9/25.</p>
 */
public class OrderYetSelectSetMealPopWindow extends PopupWindow {
    private OrderYetSelectSetMealListAdapter listViewAdapter;
    private TextView title;
    private ListView listView;
    private ImageView img_delete;           //关闭按钮
    private Activity context;
    private OnCheckedListener listener;
    private List<SetMealData> lists;

    public OrderYetSelectSetMealPopWindow(Activity context) {
        this.context = context;
    }

    public void init() {

        View view = LayoutInflater.from(context).inflate(R.layout.pop_window_yet_select_set_meal, null, false);
        this.setContentView(view);
        lists = new ArrayList<>();

        title = (TextView) view.findViewById(R.id.set_meal_name);
        listView = (ListView) view.findViewById(R.id.set_meal_list);
        img_delete = (ImageView) view.findViewById(R.id.img_delete);
        listViewAdapter = new OrderYetSelectSetMealListAdapter(context,lists);
        listView.setAdapter(listViewAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                if (listener != null){
//                    listener.onChecked(position);
//                }
//                closePopupWindow();
//            }
//        });

        listViewAdapter.setDeleteOnClickListener(new OrderYetSelectSetMealListAdapter.DeleteOnClickListener() {
            @Override
            public void onClickDelete(int position) {

            }
        });


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


//        setListViewHeightBasedOnChildren(listView);

    }

    public void setData(List<SetMealData> list,String content) {
//        title.setText(content);
        if (lists != null && lists.size() > 0){
            lists.clear();
        }
        lists.addAll(list);
//        listViewAdapter.updata(lists);
        listViewAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(listView);
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


    /**
     * @param listener the listener to set
     */
    public void setListener(OnCheckedListener listener) {
        this.listener = listener;
    }

    public interface OnCheckedListener {
        /**
         * popupwindow消失时选中的内容
         */
        void onChecked(int index);
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

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        if (params.height > dip2px(context,255f)) {
            params.height = dip2px(context,255f);
        }
        listView.setLayoutParams(params);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
