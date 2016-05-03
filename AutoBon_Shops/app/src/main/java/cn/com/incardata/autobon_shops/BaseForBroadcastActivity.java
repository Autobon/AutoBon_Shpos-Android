package cn.com.incardata.autobon_shops;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import cn.com.incardata.fragment.OrderFinishedDialogFragmnet;

/**可弹出订单完成通知框的基类
 * Created by wanghao on 16/4/26.
 */
public class BaseForBroadcastActivity extends BaseActivity{
    /** 订单完成广播 */
    public static final String ACTION_ORDER_FINISHED = "cn.com.incardata.ORDER_FINISHED";

    private OrderFinishedDialogFragmnet mOrderFinishedDialogFragmnet;

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mOrderReceiver, getOrderFinishedIntentFilter());
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mOrderReceiver);
    }

    /**
     * 订单完成广播接收
     * @return
     */
    public static IntentFilter getOrderFinishedIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_ORDER_FINISHED);
        return intentFilter;
    }

    private final BroadcastReceiver mOrderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && "FINISHED".equals(intent.getStringExtra("action"))){
                if (mOrderFinishedDialogFragmnet == null){
                    mOrderFinishedDialogFragmnet = new OrderFinishedDialogFragmnet();
                }
                mOrderFinishedDialogFragmnet.show(getFragmentManager(), "FINISHED");
                mOrderFinishedDialogFragmnet.setData(intent.getIntExtra("OrderId", -1), intent.getStringExtra("OrderNum"));
            }
        }
    };
}
