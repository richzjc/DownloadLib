package com.richzjc.download.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.richzjc.download.RDownloadClient;
import com.richzjc.download.notify.Observer;
import com.richzjc.download.task.ParentTask;

public class MainHandler extends Handler {

    public static final int NOTIFY_PROGRESS = 1;
    public static final int NOTIFY_STATUS = 2;
    public static final int NOTIFY_REQUEST = 3;
    public static final int HANDLE_NEXT_MSG = 4;

    static {
        handler = new MainHandler(Looper.getMainLooper());
    }

    private static MainHandler handler;
    public static MainHandler getInstance(){
        return handler;
    }

    private MainHandler(@NonNull Looper looper) {
        super(looper);
    }

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
                case HANDLE_NEXT_MSG:
                    handleNextMsg((RDownloadClient.Builder)msg.obj);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleNextMsg(RDownloadClient.Builder builder) {
        if (builder != null && builder.getRunning().size() > 0) {
            builder.getOkHttpClient().dispatcher().executorService().execute(builder.getRunning().get(0));
        }
    }

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
}
