package com.richzjc.download.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.richzjc.download.NetWorkType;

public class NetworkUtils {

    /**
     * 网络是否可用
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr == null) return false;
        // 返回所有网络信息
        NetworkInfo[] info = connMgr.getAllNetworkInfo();
        if (info != null) {
            for (NetworkInfo anInfo : info) {
                if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前的网络类型：<br/> -1：没有网络 <br/>1：WIFI网络<br/>2：wap 网络<br/>3：net网络
     */
    @SuppressLint("MissingPermission")
    public static NetWorkType getNetWorkType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr == null) return NetWorkType.NONE;
        // 获取当前激活的网络连接信息
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NetWorkType.NONE;
        }
        int nType = networkInfo.getType();

        if (nType == ConnectivityManager.TYPE_MOBILE) {
            return NetWorkType.MOBILE;
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetWorkType.WIFI;
        }
        return NetWorkType.NONE;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Context context, int requestCode) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

}

