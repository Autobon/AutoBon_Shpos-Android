package cn.com.incardata.getui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.incardata.autobon_shops.BaseForBroadcastActivity;
import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.utils.L;

public class GeTuiReceiver extends BroadcastReceiver {
    public GeTuiReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)){
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);
                    if (TextUtils.isEmpty(data)){
                        Log.d("Getui", "receiver payload : data = null");
                        return;
                    }
                    processMessage(context, data);
                    Log.d("Getui", "receiver payload : " + data);
                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                L.d("Getui", cid);
                break;

            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 *
                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
                 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
                 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;
        }
    }

    private void processMessage(Context context, String msg){
        try {
            JSONObject jsonObject = new JSONObject(msg);
            String action = jsonObject.getString("action");

            if ("NEW_MESSAGE".equals(action)){//系统通知
                showNotification(context, jsonObject.getJSONObject("message").getString("title"), jsonObject.getJSONObject("message").getString("content"), 1);
            }else if("VERIFICATION_FAILED".equals(action)){//认证失败
                showNotification(context, jsonObject.getString("title"), "", 2);
            }else if ("VERIFICATION_SUCCEED".equals(action)){//认证成功
                showNotification(context, jsonObject.getString("title"), "", 2);
            }else if ("ORDER_REMIND".equals(action)){//订单超时和订单施工提醒
                showNotification(context,"订单提醒" , jsonObject.getString("title"), 5);
            }else if ("ORDER_COMPLETE".equals(action)) {
                JSONObject order = jsonObject.getJSONObject("order");
                if (order == null) return;
                if ("FINISHED".equals(order.getString("status"))) {//订单完成
                    Intent intent = new Intent(BaseForBroadcastActivity.ACTION_ORDER_FINISHED);
                    intent.putExtra("action", "FINISHED");
                    intent.putExtra("OrderId", order.getInt("id"));
                    intent.putExtra("OrderNum", order.getString("orderNum"));
                    context.sendBroadcast(intent);

                    showNotification(context, "订单完成", jsonObject.getString("title"), 3);
                } else if ("TAKEN_UP".equals(order.getString("status"))) {//技师已接单
                    showNotification(context, "已接单", jsonObject.getString("title"), 4);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Getui", "透传的josn格式错误");
        }
    }

    private void showNotification(Context context, String title, String message, int nId) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, nId, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setTicker(title)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            n = builder.getNotification();
        } else {
            n = builder.build();
        }
        mNotificationManager.notify(nId, n);
    }
}
