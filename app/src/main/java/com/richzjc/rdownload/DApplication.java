package com.richzjc.rdownload;

import android.app.Application;

import com.netease.eventbus.demo.MyEventBusIndex;
import com.richzjc.download.RDownloadClient;


public class DApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RDownloadClient.Companion.addIndex(new MyEventBusIndex());
        RDownloadClient.Companion.addIndex(new com.netease.eventbus.nextapp.MyEventBusIndex());
    }
}
