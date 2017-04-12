package com.zjwocai.qundui.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.threeti.teamlibrary.activity.BaseActivity;
import com.threeti.teamlibrary.finals.ProjectConstant;
import com.threeti.teamlibrary.model.UserModel;
import com.threeti.teamlibrary.utils.SPUtil;
import com.zjwocai.qundui.QunDuiApplication;
import com.zjwocai.qundui.activity.goods.OrderDetailActivity;
import com.zjwocai.qundui.activity.home.NewsDetailActivity;
import com.zjwocai.qundui.activity.login.LoginActivity;
import com.zjwocai.qundui.activity.money.IAEBFormActivity;
import com.zjwocai.qundui.fragment.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义提醒: " + bundle.getString(JPushInterface.EXTRA_ALERT));
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            UserModel userinfo = SPUtil.getObjectFromShare(ProjectConstant.KEY_USERINFO);
            if (null != userinfo) {
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                try {
                    JSONObject data = new JSONObject(extras);
                    String type = data.getString("type");
                    Log.d(TAG, "type=" + type);
                    Map map = new HashMap();
                    switch (type) {
                        case "1"://订单
                            String id = data.getString("orderid");
                            if (null != id && !id.isEmpty()) {
                                map.put("state", 1);
                                map.put("from", "4");
                                map.put("id", id);
                                Intent i1 = new Intent(context,
                                        OrderDetailActivity.class);
                                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                if (null == QunDuiApplication.getInstance().getActivityManager().currentActivity()) {
                                    i1.putExtra("data", (Serializable) map);
                                    context.startActivity(i1);
                                } else {
                                    startActivity(OrderDetailActivity.class, id, 1);
                                }
                            }

                            break;
                        case "2"://支付运费
                            String id1 = data.getString("orderid");
                            if (null != id1 && !id1.isEmpty()) {
                                map.put("state", 3);
                                map.put("from", "4");
                                map.put("id", id1);
                                Intent i2 = new Intent(context,
                                        OrderDetailActivity.class);
                                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                if (null == QunDuiApplication.getInstance().getActivityManager().currentActivity()) {
                                    i2.putExtra("data", (Serializable) map);
                                    context.startActivity(i2);
                                } else {
                                    startActivity(OrderDetailActivity.class, id1, 2);
                                }
                            }
                            break;
                        case "3"://提现打款
                            Intent i3 = new Intent(context,
                                    IAEBFormActivity.class);
                            i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            if (null == QunDuiApplication.getInstance().getActivityManager().currentActivity()) {
                                i3.putExtra("data", (Serializable) "-1");
                                context.startActivity(i3);
                            } else {
                                startActivity(IAEBFormActivity.class, "", 0);
                            }
                            break;
                        case "4"://自定义推送
                            Intent i4 = new Intent(context,
                                    NewsDetailActivity.class);
                            i4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            if (null == QunDuiApplication.getInstance().getActivityManager().currentActivity()) {
                                map.put("content",bundle.getString(JPushInterface.EXTRA_ALERT));
                                map.put("flag","-1");
                                i4.putExtra("data", (Serializable) map);
                                context.startActivity(i4);
                            } else {
                                Activity activity = QunDuiApplication.getInstance().getActivityManager().currentActivity();
                                if (activity instanceof BaseActivity) {
                                    BaseActivity baseActivity = (BaseActivity) activity;
                                    map.put("content",bundle.getString(JPushInterface.EXTRA_ALERT));
                                    map.put("flag","0");
                                    baseActivity.startActivity(NewsDetailActivity.class,map);
                                }
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Intent i = new Intent(context,
                        LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
    /**
     * 处于应用中时进行跳转
     */
    private void startActivity(Class cla, String id, int state) {
        Activity activity = QunDuiApplication.getInstance().getActivityManager().currentActivity();
        if (!id.isEmpty()) {
            Map map = new HashMap();
            map.put("id", id);
            map.put("state", state);
            if (activity instanceof OrderDetailActivity) {
                OrderDetailActivity orderDetailActivity = (OrderDetailActivity) activity;
                orderDetailActivity.startSelf(id, state);
            } else if (activity instanceof MainActivity) {
                MainActivity homeActivity = (MainActivity) activity;
                map.put("from", "4");
                homeActivity.startActivity(cla, map);

            } else if (activity instanceof BaseActivity) {
                map.put("from", "5");
                BaseActivity baseActivity = (BaseActivity) activity;
                baseActivity.startActivityForResult(cla, map, 10086);
            }
        } else {
            if (activity instanceof BaseActivity) {
                BaseActivity baseActivity = (BaseActivity) activity;
                baseActivity.startActivity(cla, "0");
            }
        }

    }

//    /**
//     * 处于应用中时进行跳转
//     */
//    private void startActivity(Class cla, String id, int state) {
//        Activity activity = QunDuiApplication.getInstance().getActivityManager().currentActivity();
//        if (!id.isEmpty()) {
//            Map map = new HashMap();
//            map.put("id", id);
//            map.put("state", state);
//            if (activity instanceof OrderDetailActivity) {
//                OrderDetailActivity orderDetailActivity = (OrderDetailActivity) activity;
//                orderDetailActivity.startSelf(id, state);
//            } else if (activity instanceof MainActivity) {
//                MainActivity homeActivity = (MainActivity) activity;
//                map.put("from", "4");
//                homeActivity.startActivity(cla, map);
////            } else if (activity instanceof HurryActivity) {
////                HurryActivity hurryActivity = (HurryActivity) activity;
////                map.put("from", "6");
////                hurryActivity.startActivity(cla, map);
//            } else if (activity instanceof GoodsActivity) {
//                GoodsActivity goodsActivity = (GoodsActivity) activity;
//                map.put("from", goodsActivity.getFrom());
//                goodsActivity.startActivity(cla, map);
//            } else if (activity instanceof BaseActivity) {
//                map.put("from", "5");
//                BaseActivity baseActivity = (BaseActivity) activity;
//                baseActivity.startActivityForResult(cla, map, 10086);
//            }
//        } else {
//            if (activity instanceof BaseActivity) {
//                BaseActivity baseActivity = (BaseActivity) activity;
//                baseActivity.startActivity(cla, "0");
//            }
//        }
//
//    }
}
