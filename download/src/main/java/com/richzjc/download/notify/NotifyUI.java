package com.richzjc.download.notify;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;

import com.richzjc.download.RDownloadClient;
import com.richzjc.download.task.ParentTask;

import static com.richzjc.download.okhttp.MainHandler.NOTIFY_PROGRESS;
import static com.richzjc.download.okhttp.MainHandler.NOTIFY_REQUEST;
import static com.richzjc.download.okhttp.MainHandler.NOTIFY_STATUS;


public class NotifyUI {

    public static void notifyProgress(RDownloadClient.Builder builder, ParentTask r) {
        Message msg = new Message();
        msg.what = NOTIFY_PROGRESS;
        msg.obj = r;
        builder.getHandler().sendMessage(msg);
    }

    public static void notifyRequestData(RDownloadClient.Builder builder, ParentTask r) {
        Message msg = new Message();
        msg.what = NOTIFY_REQUEST;
        msg.obj = r;
        builder.getHandler().sendMessage(msg);
    }

    public static void notifyStatusChange(RDownloadClient.Builder builder, ParentTask r) {
        Message msg = new Message();
        msg.what = NOTIFY_STATUS;
        msg.obj = r;
        builder.getHandler().sendMessage(msg);
    }
}
