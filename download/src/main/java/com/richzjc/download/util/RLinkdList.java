package com.richzjc.download.util;

import androidx.annotation.NonNull;
import com.richzjc.download.RDownloadClient;
import com.richzjc.download.notify.NotifyUI;

import java.util.Collection;
import java.util.LinkedList;

public class RLinkdList<T> extends LinkedList<T> {

    RDownloadClient.Builder builder;

    @Override
    public boolean add(T t) {
        boolean flag = super.add(t);
        if(flag)
            check();
        return flag;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> c) {
        boolean flag =  super.addAll(c);
        if(flag)
            check();
        return flag;
    }

    @Override
    public void add(int index, T element) {
        super.add(index, element);
        check();
    }

    private void check(){
        if(!builder.getRunning().isEmpty() && !builder.getPauseAndError().isEmpty())
            NotifyUI.notifyAllPauseStart(builder);
    }
}
