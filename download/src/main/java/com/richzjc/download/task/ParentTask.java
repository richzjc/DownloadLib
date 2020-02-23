package com.richzjc.download.task;

import java.util.List;

public abstract class ParentTask implements IParentTask, Runnable{

    public int progress = 0;
    public int status = 0;
    public long totalLength;
    public long downloadLength;


    @Override
    public void run() {
        List<ChildTask> childTasks = getChildTasks();
        //TODO 对每一个Task进行下载
    }
}
