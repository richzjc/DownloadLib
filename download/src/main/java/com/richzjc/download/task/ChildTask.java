package com.richzjc.download.task;

import com.richzjc.download.RDownloadClient;
import com.richzjc.download.util.RequestUtilKt;

public class ChildTask implements IChildTask{

    public String requestUrl;
    public String fileName;
    public long totalLenght;
    public long downloadLenght;
    public String filePath;

    public ChildTask(String requestUrl, String fileName){
        this.requestUrl = requestUrl;
        this.fileName = fileName;
    }

    public ChildTask(String requestUrl){
        this.requestUrl = requestUrl;
    }

    @Override
    public boolean run(RDownloadClient.Builder builder, ParentTask parentTask) {
        return RequestUtilKt.download(builder, parentTask, this);
    }
}
