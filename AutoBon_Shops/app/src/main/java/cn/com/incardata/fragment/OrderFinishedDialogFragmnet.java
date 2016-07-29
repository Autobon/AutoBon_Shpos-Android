package cn.com.incardata.fragment;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.com.incardata.autobon_shops.GoCommentActivity;
import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.utils.T;

/**
 * Created by wanghao on 16/5/1.
 */
public class OrderFinishedDialogFragmnet extends DialogFragment implements View.OnClickListener{

    private View rootView;
    private TextView tips;
    private String orderNum;
    private int orderId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragment_Cancelable);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_order_finished_dialog, container, false);
            initViews();
        }else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null) {
                parent.removeView(rootView);
            }
        }
        if (!TextUtils.isEmpty(orderNum)){
            String str = getString(R.string.order_finished_dialog_tips, this.orderNum);
            tips.setText(str);
        }
        return rootView;
    }

    private void initViews() {
        tips = (TextView) rootView.findViewById(R.id.order_finished_serial);
        rootView.findViewById(R.id.dialog_close).setOnClickListener(this);
        rootView.findViewById(R.id.immediation_comment).setOnClickListener(this);
    }

    public void setData(int orderId, String orderNum){
        this.orderId = orderId;
        this.orderNum = orderNum;
        if (tips != null){
            String str = getString(R.string.order_finished_dialog_tips, this.orderNum);
            tips.setText(str);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_close:
                dismiss();
                break;
            case R.id.immediation_comment:
                if (orderId == -1) {
                    T.show(getActivity(), R.string.loading_data_failed);
                    return;
                }
                Intent intent = new Intent(getActivity(), GoCommentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("OrderId", orderId);
                startActivity(intent);
                dismiss();
                break;
        }
    }
}
