package cn.com.incardata.view;

import android.app.Activity;
import android.content.ComponentName;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.adapter.SelectPopupListViewAdapter;
import cn.com.incardata.autobon_shops.R;


/**
 * 状态选择弹出框
 * <p>Created by wangyang on 2018/6/14.</p>
 */
public class SelectPopupView extends PopupWindow {
    private SelectPopupListViewAdapter listViewAdapter;
    private TextView title;
    private ListView listView;
    private Activity context;
    private OnCheckedListener listener;

    public SelectPopupView(Activity context) {
        this.context = context;
    }



    public void init(){

        View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_status,null,false);
        this.setContentView(view);

        title = (TextView) view.findViewById(R.id.title1);
        listView = (ListView) view.findViewById(R.id.list_item1);
        listViewAdapter = new SelectPopupListViewAdapter(context);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (listener != null){
                    listener.onChecked(position);
                }
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

    public void setData(List<String> lists,String content, int position){
        title.setText(content);
        listViewAdapter.updata(lists,position);
        listViewAdapter.notifyDataSetChanged();
    }


    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        WindowManager.LayoutParams params=context.getWindow().getAttributes();
        params.alpha=0.7f;
        context.getWindow().setAttributes(params);
    };




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


    /**
     * @param listener the listener to set
     */
    public void setListener(OnCheckedListener listener) {
        this.listener = listener;
    }

    public interface OnCheckedListener{
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
            int desiredWidth= View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }




}
