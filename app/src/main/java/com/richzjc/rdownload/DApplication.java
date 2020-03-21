package com.richzjc.rdownload;

import android.app.Application;


import com.richzjc.download.NetWorkType;
import com.richzjc.download.RDownloadClient;

import app.RIndex;


public class DApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RDownloadClient.Companion.addIndex(new RIndex());
        new RDownloadClient.Builder()
                .setMaxDownloadCount(100)
                .setThreadCount(4)
                .setNetworkType(NetWorkType.WIFI)
                .build(this);
    }
}
