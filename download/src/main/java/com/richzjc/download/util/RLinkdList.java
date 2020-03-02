package com.richzjc.download.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.richzjc.download.RDownloadClient;
import com.richzjc.download.notify.NotifyUI;

import java.util.Collection;
import java.util.LinkedList;

public class RLinkdList<T> extends LinkedList<T> {

    RDownloadClient.Builder builder;
    public RLinkdList(RDownloadClient.Builder builder){
        this.builder = builder;
    }

    @Override
    public boolean add(T t) {
        int size = size();
        boolean flag = super.add(t);
        if (flag && size == 0)
            check();
        return flag;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> c) {
        int size = size();
        boolean flag = super.addAll(c);
        if (flag && size() == 0)
            check();
        return flag;
    }

    @Override
    public void add(int index, T element) {
        int size = size();
        super.add(index, element);
        if (size == 0)
            check();
    }


    @Override
    public T remove() {
        int originSize = size();
        T object = super.remove();
        int newSize = size();
        checkRemove(originSize, newSize);
        return object;
    }

    @Override
    public T remove(int index) {
        int originSize = size();
        T object =  super.remove(index);
        int newSize = size();
        checkRemove(originSize, newSize);
        return object;
    }

    @Override
    public boolean remove(@Nullable Object o) {
        int originSize = size();
        boolean flag = super.remove(o);
        int newSize = size();
        checkRemove(originSize, newSize);
        return flag;
    }

    @Override
    public T removeFirst() {
        int originSize = size();
        T object = super.removeFirst();
        int newSize = size();
        checkRemove(originSize, newSize);
        return object;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        int originSize = size();
        boolean flag = super.removeAll(c);
        int newSize = size();
        checkRemove(originSize, newSize);
        return flag;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        int originSize = size();
        super.removeRange(fromIndex, toIndex);
        int newSize = size();
        checkRemove(originSize, newSize);
    }

    @Override
    public T removeLast() {
        int originSize = size();
        T object = super.removeLast();
        int newSize = size();
        checkRemove(originSize, newSize);
        return  object;
    }

    private void check() {
        if (!builder.getRunning().isEmpty() && !builder.getPauseAndError().isEmpty())
            NotifyUI.notifyAllPauseStart(builder);

        checkPauseOrStart();
    }

    private void checkRemove(int originSize, int newSize){
        if(originSize > 0 && newSize == 0)
            checkPauseOrStart();
    }

    private void checkPauseOrStart(){
        NotifyUI.notifyAllPauseOrStart(builder);
    }
}
