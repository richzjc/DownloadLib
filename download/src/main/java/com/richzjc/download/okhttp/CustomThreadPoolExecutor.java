package com.richzjc.download.okhttp;

import com.richzjc.download.task.ParentTask;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

public class CustomThreadPoolExecutor extends ThreadPoolExecutor {

    @NotNull
    public OkHttpClient okHttpClient;

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
        if(r instanceof ParentTask){
            checkedCache();
            checkChildTaskIsEmpty((ParentTask) r);
            checkHasTotalLength((ParentTask) r);
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        //TODO 保存数据， 更新状态
        if(r instanceof ParentTask){

        }
    }

    private void checkedCache(){
        //TODO 先判断是否有缓存
    }

    private void checkChildTaskIsEmpty(ParentTask r) {
        if(r.getChildTasks() != null && r.getChildTasks().size() > 0)
            return;


    }

    private void checkHasTotalLength(ParentTask task){
        //TODO 获取所有Task的总长度
    }
}
