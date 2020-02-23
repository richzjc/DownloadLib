package com.richzjc.download.okhttp;

public class RealCall implements Call{

    boolean isCancel;

    private RealCall(){

    }

    static RealCall newRealCall(){
        return new RealCall();
    }

    @Override
    public void cancel() {

    }

    @Override
    public void isCancel() {

    }

    @Override
    public void enqueue() {

    }
}
