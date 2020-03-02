package com.richzjc.download.task;

import com.richzjc.download.ConstKt;
import com.richzjc.download.RDownloadClient;
import com.richzjc.download.notify.NotifyUI;
import com.richzjc.download.notify.Observer;
import com.richzjc.download.okhttp.MainHandler;
import com.richzjc.download.util.SaveDataUtilKt;

import java.util.ArrayList;
import java.util.List;

public abstract class ParentTask implements IParentTask, Runnable {

    private int progress = 0;
    private int status = 0;
    public long totalLength;
    private long downloadLength;
    private List<Observer> observers;
    private RDownloadClient.Builder builder;

    public void bindBuilder(RDownloadClient.Builder builder){
        this.builder = builder;
    }

    public int getProgress() {
        return progress;
    }

    public long getDownloadLength(){
        return downloadLength;
    }

    public void setDownloadLength(long downloadLength){
        this.downloadLength = downloadLength;
        if (totalLength != 0) {
            progress = Math.round(downloadLength * 100f / totalLength);
        } else {
            progress = 0;
        }
        NotifyUI.notifyProgress(this);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        NotifyUI.notifyStatusChange(this);
        if (status == ConstKt.DOWNLOAD_DELETE)
            SaveDataUtilKt.deleteData(this);
    }


    public List<Observer> getObservers() {
        if (observers == null)
            observers = new ArrayList<>();
        return observers;
    }


    public void registObserver(Observer observer) {
        if (observers == null)
            observers = new ArrayList<>();

        if (!observers.contains(observer))
            observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        if (observers == null)
            observers = new ArrayList<>();

        if (observers.contains(observer))
            observers.remove(observer);
    }

    @Override
    public void run() {
        List<ChildTask> childTasks = getChildTasks();
        if (childTasks != null) {
            for (ChildTask childTask : childTasks) {
                if (checkCanDownload()) {
                    if (!childTask.run(builder, this))
                        break;
                }
            }
        }
    }

    public boolean checkCanDownload(){
        if(status == ConstKt.WAITING || status == ConstKt.DOWNLOADING){
            return true;
        }else{
            return false;
        }
    }
}
