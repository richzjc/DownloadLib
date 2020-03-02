package com.richzjc.download.util;

import androidx.annotation.NonNull;
import com.richzjc.download.RDownloadClient;
import com.richzjc.download.notify.NotifyUI;

import java.util.Collection;
import java.util.LinkedList;

public class RLinkdList extends LinkedList {

    RDownloadClient.Builder builder;

    @Override
    public boolean add(Object o) {
        boolean flag =  super.add(o);
        if(flag)
            check();
        return flag;

    }

    @Override
    public boolean addAll(@NonNull Collection c) {
        boolean flag =  super.addAll(c);
        if(flag)
            check();
        return flag;
    }

    private void check(){
        if(!builder.getRunning().isEmpty() && !builder.getPauseAndError().isEmpty())
            NotifyUI.notifyAllPauseStart(builder);
    }
}
