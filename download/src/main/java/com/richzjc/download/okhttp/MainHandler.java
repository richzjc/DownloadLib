package com.richzjc.download.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;

import com.richzjc.download.NetWorkType;
import com.richzjc.download.RDownloadClient;
import com.richzjc.download.eventbus.SimpleSubscribeInfo;
import com.richzjc.download.eventbus.SubscribeMethod;
import com.richzjc.download.eventbus.WrapNotifyModel;
import com.richzjc.download.notify.Observer;
import com.richzjc.download.task.ParentTask;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainHandler extends Handler {

    public static final int NOTIFY_PROGRESS = 1;
    public static final int NOTIFY_STATUS = 2;
    public static final int NOTIFY_REQUEST = 3;
    public static final int HANDLE_NEXT_MSG = 4;
    public static final int NOTIFY_SINGLE_PAGE = 5;
    public static final int NOTIFY_ALL_SIZECHANGE_PAGE = 6;
    public static final int NOTIFY_ALL_PAUSE_START = 7;
    public static final int NOTIFY_ALL_PAUSE_OR_START = 8;
    public static final int NOTIFY_NET_CHANGE = 9;
    public static final int NOTIFY_PAUSE_START_EMPTY = 10;

    static {
        handler = new MainHandler(Looper.getMainLooper());
    }

    private static MainHandler handler;

    public static MainHandler getInstance() {
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
                    handleNextMsg((RDownloadClient.Builder) msg.obj);
                    break;
                case NOTIFY_SINGLE_PAGE:
                    handleNotifySinglePage((WrapNotifyModel) msg.obj);
                    break;
                case NOTIFY_ALL_SIZECHANGE_PAGE:
                    handleNotifyAllSizeChange((String) msg.obj);
                    break;
                case NOTIFY_ALL_PAUSE_START:
                    handleNotifyPauseStart((String) msg.obj);
                    break;
                case NOTIFY_ALL_PAUSE_OR_START:
                    handlePauseOrStart((RDownloadClient.Builder) msg.obj);
                    break;
                case NOTIFY_NET_CHANGE:
                    handleNetChange((NetWorkType)msg.obj);
                    break;
                case NOTIFY_PAUSE_START_EMPTY:
                    handlePauseStartEmpty((RDownloadClient.Builder) msg.obj);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePauseStartEmpty(RDownloadClient.Builder obj) {
        Map<Object, SimpleSubscribeInfo> map = RDownloadClient.Companion.getSubscribeInfos().get(obj.getConfigurationKey());
        if (obj.getRunning().size() == 0 && obj.getPauseAndError().size() == 0) {
            if (map != null) {
                for (Map.Entry<Object, SimpleSubscribeInfo> entry : map.entrySet()) {
                    List<SubscribeMethod> sizeMethod = entry.getValue().getPauseStartEmpty();
                    if (sizeMethod != null) {
                        for (SubscribeMethod subscribeMethod : sizeMethod) {
                            try {
                                Method method = entry.getKey().getClass().getDeclaredMethod(subscribeMethod.getMethodName());
                                method.setAccessible(true);
                                method.invoke(entry.getKey());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleNetChange(NetWorkType obj) {
       Map<String, HashMap<Object, SimpleSubscribeInfo>> map = RDownloadClient.Companion.getSubscribeInfos();
       if(map != null){
           for(Map.Entry<String, HashMap<Object, SimpleSubscribeInfo>> entry : map.entrySet()){
               for(Map.Entry<Object, SimpleSubscribeInfo> subEntry : entry.getValue().entrySet()){
                   if(subEntry.getValue() != null && subEntry.getValue().getNetChangeMethod() !=  null){
                       for(SubscribeMethod subscribeMethod : subEntry.getValue().getNetChangeMethod()){
                           try {
                               Method method = subEntry.getKey().getClass().getDeclaredMethod(subscribeMethod.getMethodName(), NetWorkType.class);
                               method.setAccessible(true);
                               method.invoke(subEntry.getKey(), obj);
                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                       }
                   }
               }
           }
       }
    }

    private void handlePauseOrStart(RDownloadClient.Builder obj) {
        Map<Object, SimpleSubscribeInfo> map = RDownloadClient.Companion.getSubscribeInfos().get(obj.getConfigurationKey());
        if (obj.getRunning().size() > 0 && obj.getPauseAndError().size() == 0) {
            if (map != null) {
                for (Map.Entry<Object, SimpleSubscribeInfo> entry : map.entrySet()) {
                    List<SubscribeMethod> sizeMethod = entry.getValue().getStartAllMethod();
                    if (sizeMethod != null) {
                        for (SubscribeMethod subscribeMethod : sizeMethod) {
                            try {
                                Method method = entry.getKey().getClass().getDeclaredMethod(subscribeMethod.getMethodName());
                                method.setAccessible(true);
                                method.invoke(entry.getKey());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } else if (obj.getPauseAndError().size() > 0 && obj.getRunning().size() == 0) {
            if (map != null) {
                for (Map.Entry<Object, SimpleSubscribeInfo> entry : map.entrySet()) {
                    List<SubscribeMethod> sizeMethod = entry.getValue().getPauseAllMethod();
                    if (sizeMethod != null) {
                        for (SubscribeMethod subscribeMethod : sizeMethod) {
                            try {
                                Method method = entry.getKey().getClass().getDeclaredMethod(subscribeMethod.getMethodName());
                                method.setAccessible(true);
                                method.invoke(entry.getKey());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleNotifyPauseStart(String configrationKey) {
        Map<Object, SimpleSubscribeInfo> map = RDownloadClient.Companion.getSubscribeInfos().get(configrationKey);
        if (map != null) {
            for (Map.Entry<Object, SimpleSubscribeInfo> entry : map.entrySet()) {
                List<SubscribeMethod> sizeMethod = entry.getValue().getPauseStart();
                if (sizeMethod != null) {
                    for (SubscribeMethod subscribeMethod : sizeMethod) {
                        try {
                            Method method = entry.getKey().getClass().getDeclaredMethod(subscribeMethod.getMethodName());
                            method.setAccessible(true);
                            method.invoke(entry.getKey());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void handleNotifyAllSizeChange(String configurationKey) {
        Map<Object, SimpleSubscribeInfo> map = RDownloadClient.Companion.getSubscribeInfos().get(configurationKey);
        RDownloadClient client = RDownloadClient.Companion.getClient(configurationKey);
        if (client == null || map == null)
            return;
        int downloadSize = client.getAllDownloadSize();
        for (Map.Entry<Object, SimpleSubscribeInfo> entry : map.entrySet()) {
            List<SubscribeMethod> sizeMethod = entry.getValue().getSizeChangeMethod();
            if (sizeMethod != null) {
                for (SubscribeMethod subscribeMethod : sizeMethod) {
                    try {
                        Method method = entry.getKey().getClass().getDeclaredMethod(subscribeMethod.getMethodName(), int.class);
                        method.setAccessible(true);
                        method.invoke(entry.getKey(), downloadSize);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void handleNotifySinglePage(WrapNotifyModel wrapNotifyModel) {
        if (wrapNotifyModel != null) {
            SimpleSubscribeInfo subscribeInfo = RDownloadClient.Companion.getCallbackMethods().get(wrapNotifyModel.getObj().getClass());
            if(subscribeInfo == null)
                return;
            List<SubscribeMethod> sizeMethod = subscribeInfo.getSizeChangeMethod();
            int allDownloadSize = wrapNotifyModel.getClient().getAllDownloadSize();
            if (sizeMethod != null) {
                for (SubscribeMethod subscribeMethod : sizeMethod) {
                    try {
                        Method method = wrapNotifyModel.getObj().getClass().getDeclaredMethod(subscribeMethod.getMethodName(), int.class);
                        method.setAccessible(true);
                        method.invoke(wrapNotifyModel.getObj(), allDownloadSize);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            List<SubscribeMethod> pauseAllMethods = subscribeInfo.getPauseAllMethod();
            List<ParentTask> running = wrapNotifyModel.getClient().getRunningData();
            List<ParentTask> pause = wrapNotifyModel.getClient().getPauseOrErrorData();
            if ((running == null || running.isEmpty()) && (pause != null && !pause.isEmpty())) {
                for (SubscribeMethod subscribeMethod : pauseAllMethods) {
                    try {
                        Method method = wrapNotifyModel.getObj().getClass().getDeclaredMethod(subscribeMethod.getMethodName());
                        method.setAccessible(true);
                        method.invoke(wrapNotifyModel.getObj());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if ((pause == null || pause.isEmpty()) && (running != null && !running.isEmpty())) {
                List<SubscribeMethod> startAllMethod = subscribeInfo.getStartAllMethod();
                for (SubscribeMethod subscribeMethod : startAllMethod) {
                    try {
                        Method method = wrapNotifyModel.getObj().getClass().getDeclaredMethod(subscribeMethod.getMethodName());
                        method.setAccessible(true);
                        method.invoke(wrapNotifyModel.getObj());
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
            for (Observer observer : task.getObservers()) {
                observer.notifyProgress();
            }
        }
    }

    private void handleStatus(ParentTask task) {
        if (task != null && task.getObservers() != null) {
            Log.i("status", "handleStatus:" + task.getStatus());
            for (Observer observer : task.getObservers()) {
                observer.notifyStatus();
            }
        }
    }

    private void handleRequest(ParentTask task) {
        if (task != null && task.getObservers() != null) {
            for (Observer observer : task.getObservers()) {
                observer.notifyRequestData();
            }
        }
    }
}
