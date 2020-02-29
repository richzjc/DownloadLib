package com.richzjc.download.task;

import com.richzjc.download.notify.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class ParentTask implements IParentTask, Runnable {

    public int progress = 0;
    public int status = 0;
    public long totalLength;
    public long downloadLength;
    private List<Observer> observers;

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
        //TODO 对每一个Task进行下载
    }
}
