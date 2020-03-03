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

    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        if (r instanceof ParentTask && ((ParentTask) r).checkCanDownload()) {
           synchronized (builder){
               ((ParentTask) r).bindBuilder(builder);
               Log.i("status", "beforExecute:" +  r);
               ((ParentTask) r).setStatus(ConstKt.DOWNLOADING);
               checkedCache();
               checkChildTaskIsEmpty((ParentTask) r);
           }
        }
    }

    @Override
    protected void afterExecute(Runnable it, Throwable t) {
        super.afterExecute(it, t);
        Log.i("download", "afterExecute");
        synchronized (builder) {
            if (it instanceof ParentTask) {
                if (((ParentTask) it).getStatus() == ConstKt.DOWNLOAD_DELETE) {
                    boolean flag = builder.getRunning().remove(it);
                    boolean flag1 = builder.getPauseAndError().remove(it);
                    if (flag || flag1)
                        NotifyUI.notifyAllSizeChange(builder.getConfigurationKey());
                } else if (((ParentTask) it).getProgress() >= 100) {
                    SaveDataUtilKt.saveData((ParentTask) it);
                    ((ParentTask) it).setStatus(ConstKt.DOWNLOAD_FINISH);
                    boolean flag = builder.getRunning().remove(it);
                    boolean flag1 = builder.getPauseAndError().remove(it);
                    if (flag || flag1)
                        NotifyUI.notifyAllSizeChange(builder.getConfigurationKey());
                } else {
                    if (((ParentTask) it).getStatus() != ConstKt.DOWNLOAD_PAUSE && ((ParentTask) it).getStatus() != ConstKt.DOWNLOAD_ERROR)
                        ((ParentTask) it).setStatus(ConstKt.DOWNLOAD_ERROR);
                    SaveDataUtilKt.saveData((ParentTask) it);
                    builder.getRunning().remove(it);
                    builder.getPauseAndError().add((ParentTask) it);
                }
            }

            Message msg = new Message();
            msg.obj = builder;
            msg.what = HANDLE_NEXT_MSG;
            MainHandler.getInstance().sendMessage(msg);
        }
    }

    private void checkedCache() {
        //TODO 先判断是否有缓存
    }

    private void checkChildTaskIsEmpty(ParentTask r) {
        if (r.getRealChildTasks() != null && r.getRealChildTasks().size() > 0){
            checkHasTotalLength(r);
        }else if (r instanceof IRequestParamter) {
            boolean isSuccess = RequestUtilKt.request(builder.getOkHttpClient(), (IRequestParamter) r);
            Log.i("download", "result : " + r.toString());
            if (isSuccess) {
                NotifyUI.notifyRequestData(r);
                checkHasTotalLength(r);
            } else {
                r.setStatus(ConstKt.DOWNLOAD_ERROR);
            }
        }else{
            checkHasTotalLength(r);
        }
    }

    private void checkHasTotalLength(ParentTask r) {
        if (r.checkCanDownload()) {
            boolean isSuccess = RequestUtilKt.requestLength(builder.getOkHttpClient(), r);
            if(!isSuccess)
                r.setStatus(ConstKt.DOWNLOAD_ERROR);
        }
    }
}
