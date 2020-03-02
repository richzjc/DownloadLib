package com.richzjc.download.task;

public class ChildTask implements IChildTask{

    public String requestUrl;
    public String fileName;
    public long totalLenght;
    public long downloadLenght;

    public ChildTask(String requestUrl, String fileName){
        this.requestUrl = requestUrl;
        this.fileName = fileName;
    }

    public ChildTask(String requestUrl){
        this.requestUrl = requestUrl;
    }

    @Override
    public boolean run() {
        return false;
    }
}
