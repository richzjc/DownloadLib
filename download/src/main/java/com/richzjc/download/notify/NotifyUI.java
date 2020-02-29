package com.richzjc.download.notify;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import com.richzjc.download.task.ParentTask;


public class NotifyUI {

    private static final int NOTIFY_PROGRESS = 1;
    private static final int NOTIFY_STATUS = 2;
    private static final int NOTIFY_REQUEST = 3;


    private static Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            try {
                switch (msg.what) {
                    case NOTIFY_PROGRESS:
                        handleProgress((ParentTask) msg.obj);
                        break;
                    case NOTIFY_STATUS:
                        handleStatus((ParentTask) msg.obj);
                        break;
                    case NOTIFY_REQUEST:
                        handleRequest((ParentTask) msg.obj);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private static void handleProgress(ParentTask task) {
        if (task != null && task.getObservers() != null) {
            for(Observer observer : task.getObservers()){
                observer.notifyProgress();
            }
        }
    }

    private static void handleStatus(ParentTask task) {
        if (task != null && task.getObservers() != null) {
            for(Observer observer : task.getObservers()){
                observer.notifyStatus();
            }
        }
    }

    private static void handleRequest(ParentTask task) {
        if (task != null && task.getObservers() != null) {
            for(Observer observer : task.getObservers()){
                observer.notifyRequestData();
            }
        }
    }

    public static void notifyProgress(ParentTask r) {
        Message msg = new Message();
        msg.what = NOTIFY_PROGRESS;
        msg.obj = r;
        handler.sendMessage(msg);
    }

    public static void notifyRequestData(ParentTask r) {
        Message msg = new Message();
        msg.what = NOTIFY_REQUEST;
        msg.obj = r;
        handler.sendMessage(msg);
    }

    public static void notifyStatusChange(ParentTask r) {
        Message msg = new Message();
        msg.what = NOTIFY_STATUS;
        msg.obj = r;
        handler.sendMessage(msg);
    }
}
