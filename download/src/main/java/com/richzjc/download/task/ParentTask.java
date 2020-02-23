package com.richzjc.download.task;

public abstract class ParentTask implements IParentTask{

    public int progress = 0;
    public int status = 0;
    public long totalLength;
    public long downloadLength;

}
