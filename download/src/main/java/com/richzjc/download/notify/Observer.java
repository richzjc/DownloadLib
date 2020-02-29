package com.richzjc.download.notify;

public interface Observer {
    void notifyRequestData();
    void notifyProgress();
    void notifyStatus();
}
