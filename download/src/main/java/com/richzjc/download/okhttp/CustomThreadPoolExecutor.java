package com.richzjc.download.okhttp;

import android.os.Message;
import android.util.Log;

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

import static com.richzjc.download.okhttp.MainHandler.HANDLE_NEXT_MSG;

public class CustomThreadPoolExecutor extends ThreadPoolExecutor {

    @NotNull
    public RDownloadClient.Builder builder;

    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        //TODO 判断是否有加载完Task, 讲算totalLength 修改状态为下载中
        if (r instanceof ParentTask) {
            Log.i("download", r.toString());
            ((ParentTask) r).status = ConstKt.DOWNLOADING;
            NotifyUI.notifyStatusChange(builder, (ParentTask)r);
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

            Message msg = new Message();
            msg.obj = builder;
            msg.what = HANDLE_NEXT_MSG;
            builder.getHandler().sendMessage(msg);
        }
    }

    private void checkedCache() {
        //TODO 先判断是否有缓存
    }

    private void checkChildTaskIsEmpty(ParentTask r) {
        if (r.getChildTasks() != null && r.getChildTasks().size() > 0)
            return;

        if (r instanceof IRequestParamter) {
            boolean isSuccess = RequestUtilKt.request(builder.getOkHttpClient(), (IRequestParamter) r);
            Log.i("download", "result : " + r.toString());
            if(isSuccess)
                NotifyUI.notifyRequestData(builder, r);
        }
    }

    private void checkHasTotalLength(ParentTask task) {
        //TODO 获取所有Task的总长度
    }
}
