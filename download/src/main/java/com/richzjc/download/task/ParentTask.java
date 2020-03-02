package com.richzjc.download.task;

import com.richzjc.download.ConstKt;
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
    public long downloadLength;
    private List<Observer> observers;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
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
                if (status == ConstKt.WAITING || status == ConstKt.DOWNLOADING) {
                    if (!childTask.run())
                        break;
                }
            }
        }
    }
}
