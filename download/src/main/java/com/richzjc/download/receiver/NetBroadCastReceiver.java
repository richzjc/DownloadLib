package com.richzjc.download.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.richzjc.download.ConstKt;
import com.richzjc.download.NetWorkType;
import com.richzjc.download.notify.NotifyUI;
import com.richzjc.download.util.NetworkUtils;

public class NetBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e(ConstKt.LOG_TAG, "异常了");
            return;
        }

        // 处理广播事件
        if (intent.getAction().equalsIgnoreCase(ConstKt.ANDROID_NET_CHANGE_ACTION)) {
            Log.e(ConstKt.LOG_TAG, "网络发生改变");
            NetWorkType netType = NetworkUtils.getNetWorkType(context); // 网络类型
            if (NetworkUtils.isNetworkAvailable(context)) {
                Log.e(ConstKt.LOG_TAG, "网络连接成功");
            } else {
                Log.e(ConstKt.LOG_TAG, "没有网络连接");
            }
            // 通知所有注册的方法，网络发生了变化
            NotifyUI.notifyNetChange(netType);
        }
    }
}
