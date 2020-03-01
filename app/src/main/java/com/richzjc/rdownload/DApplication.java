package com.richzjc.rdownload;

import android.app.Application;

import com.richzjc.download.NetWorkType;
import com.richzjc.download.RDownloadClient;


public class DApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        RDownloadClient.Companion.addIndex(new MyEventBusIndex());
        new RDownloadClient.Builder().setMaxDownloadCount(100).setThreadCount(1).setNetworkType(NetWorkType.WIFI).build();
    }
}
