package com.richzjc.download.notify;

import android.os.Message;

import com.richzjc.download.RDownloadClient;
import com.richzjc.download.eventbus.WrapNotifyModel;
import com.richzjc.download.okhttp.MainHandler;
import com.richzjc.download.task.ParentTask;

import static com.richzjc.download.okhttp.MainHandler.NOTIFY_ALL_PAUSE_OR_START;
import static com.richzjc.download.okhttp.MainHandler.NOTIFY_ALL_PAUSE_START;
import static com.richzjc.download.okhttp.MainHandler.NOTIFY_ALL_SIZECHANGE_PAGE;
import static com.richzjc.download.okhttp.MainHandler.NOTIFY_PROGRESS;
import static com.richzjc.download.okhttp.MainHandler.NOTIFY_REQUEST;
import static com.richzjc.download.okhttp.MainHandler.NOTIFY_SINGLE_PAGE;
import static com.richzjc.download.okhttp.MainHandler.NOTIFY_STATUS;


public class NotifyUI {

    public static void notifyProgress(ParentTask r) {
        Message msg = new Message();
        msg.what = NOTIFY_PROGRESS;
        msg.obj = r;
       MainHandler.getInstance().sendMessage(msg);
    }

    public static void notifyRequestData(ParentTask r) {
        Message msg = new Message();
        msg.what = NOTIFY_REQUEST;
        msg.obj = r;
        MainHandler.getInstance().sendMessage(msg);
    }

    public static void notifyStatusChange(ParentTask r) {
        Message msg = new Message();
        msg.what = NOTIFY_STATUS;
        msg.obj = r;
        MainHandler.getInstance().sendMessage(msg);
    }

    public static void notifySigleChange(Object object, RDownloadClient client){
        Message msg = new Message();
        msg.what = NOTIFY_SINGLE_PAGE;
        msg.obj = new WrapNotifyModel(object, client);
        MainHandler.getInstance().sendMessage(msg);
    }

    public static void notifyAllSizeChange(String configuration){
        Message msg = new Message();
        msg.what = NOTIFY_ALL_SIZECHANGE_PAGE;
        msg.obj = configuration;
        MainHandler.getInstance().sendMessage(msg);
    }

    public static void notifyAllPauseStart(RDownloadClient.Builder builder) {
        Message msg = new Message();
        msg.what = NOTIFY_ALL_PAUSE_START;
        msg.obj = builder.getConfigurationKey();
        MainHandler.getInstance().sendMessage(msg);
    }


    public static void notifyAllPauseOrStart(RDownloadClient.Builder builder) {
        Message msg = new Message();
        msg.what = NOTIFY_ALL_PAUSE_OR_START;
        msg.obj = builder;
        MainHandler.getInstance().sendMessage(msg);
    }
}
