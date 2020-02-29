package com.richzjc.download.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.richzjc.download.ConstKt;
import com.richzjc.download.RDownloadClient;
import com.richzjc.download.notify.NotifyUI;
import com.richzjc.download.task.ParentTask;
import com.richzjc.download.util.RequestUtilKt;
import com.richzjc.download.util.SaveDataUtilKt;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomThreadPoolExecutor extends ThreadPoolExecutor {

    @NotNull
    public RDownloadClient.Builder builder;
    private Handler handler;
    private static int HANDLE_NEXT_MSG = 3;

    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        init();
    }

    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        init();
    }

    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        init();
    }

    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        init();
    }

    private void init(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (builder != null && builder.getRunning().size() > 0) {
                    builder.getOkHttpClient().dispatcher().executorService().execute(builder.getRunning().get(0));
                }
            }
        };
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        //TODO 判断是否有加载完Task, 讲算totalLength 修改状态为下载中
        if (r instanceof ParentTask) {
            ((ParentTask) r).status = ConstKt.DOWNLOADING;
            NotifyUI.notifyStatusChange();
            checkedCache();
            checkChildTaskIsEmpty((ParentTask) r);
            checkHasTotalLength((ParentTask) r);
        }
    }

    @Override
    protected void afterExecute(Runnable it, Throwable t) {
        super.afterExecute(it, t);
        synchronized (builder){
            if (it instanceof ParentTask) {
               if(((ParentTask) it).status == ConstKt.DOWNLOAD_DELETE){
                    SaveDataUtilKt.deleteData((ParentTask) it);
                    builder.getRunning().remove(it);
                    builder.getPauseAndError().remove(it);
                }else if(((ParentTask) it).progress >= 100){
                    SaveDataUtilKt.saveData((ParentTask) it);
                    ((ParentTask) it).status = ConstKt.DOWNLOAD_FINISH;
                    builder.getRunning().remove(it);
                    builder.getPauseAndError().remove(it);
                } else {
                    SaveDataUtilKt.saveData((ParentTask) it);
                    builder.getRunning().remove(it);
                    builder.getPauseAndError().add((ParentTask) it);
                }
            }

            if(handler != null){
                handler.sendEmptyMessage(HANDLE_NEXT_MSG);
            }
        }
    }

    private void checkedCache() {
        //TODO 先判断是否有缓存
    }

    private void checkChildTaskIsEmpty(ParentTask r) {
        if (r.getChildTasks() != null && r.getChildTasks().size() > 0)
            return;

        if (r instanceof IRequestParamter) {
            RequestUtilKt.request(builder.getOkHttpClient(), (IRequestParamter) r);
            Log.i("thread", "pool:" + Thread.currentThread().getName());
        }
    }

    private void checkHasTotalLength(ParentTask task) {
        //TODO 获取所有Task的总长度
    }
}
