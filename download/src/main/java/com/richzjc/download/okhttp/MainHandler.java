package com.richzjc.download.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import com.richzjc.download.RDownloadClient;
import com.richzjc.download.eventbus.SimpleSubscribeInfo;
import com.richzjc.download.eventbus.SubscribeMethod;
import com.richzjc.download.notify.Observer;
import com.richzjc.download.task.ParentTask;

import java.lang.reflect.Method;
import java.util.List;

public class MainHandler extends Handler {

    public static final int NOTIFY_PROGRESS = 1;
    public static final int NOTIFY_STATUS = 2;
    public static final int NOTIFY_REQUEST = 3;
    public static final int HANDLE_NEXT_MSG = 4;
    public static final int NOTIFY_SINGLE_PAGE = 5;

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
                case NOTIFY_SINGLE_PAGE:
                    SimpleSubscribeInfo subscribeInfo = RDownloadClient.Companion.getCallbackMethods().get(msg.obj.getClass());
                    handleNotifySinglePage(msg.obj, msg.arg1, subscribeInfo);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleNotifySinglePage(Object obj, int allDownloadSize, SimpleSubscribeInfo subscribeInfo) {
        if(subscribeInfo != null && obj != null){
            List<SubscribeMethod> sizeMethod = subscribeInfo.getSizeChangeMethod();
            if(sizeMethod != null){
                for(SubscribeMethod subscribeMethod : sizeMethod){
                    try {
                        Method method = obj.getClass().getDeclaredMethod(subscribeMethod.getMethodName(), subscribeMethod.getCls());
                        method.setAccessible(true);
                        method.invoke(obj, allDownloadSize);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void handleNextMsg(RDownloadClient.Builder builder) {
        if (builder != null && builder.getRunning().size() > 0) {
            builder.getOkHttpClient().dispatcher().executorService().execute(builder.getRunning().get(0));
        }
    }

    private void handleProgress(ParentTask task) {
        if (task != null && task.getObservers() != null) {
            for(Observer observer : task.getObservers()){
                observer.notifyProgress();
            }
        }
    }

    private void handleStatus(ParentTask task) {
        if (task != null && task.getObservers() != null) {
            for(Observer observer : task.getObservers()){
                observer.notifyStatus();
            }
        }
    }

    private void handleRequest(ParentTask task) {
        if (task != null && task.getObservers() != null) {
            for(Observer observer : task.getObservers()){
                observer.notifyRequestData();
            }
        }
    }

}
