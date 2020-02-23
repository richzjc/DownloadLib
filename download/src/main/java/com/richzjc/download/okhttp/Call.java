package com.richzjc.download.okhttp;

public interface Call {

    void cancel();
    void isCancel();
    void enqueue();
}
