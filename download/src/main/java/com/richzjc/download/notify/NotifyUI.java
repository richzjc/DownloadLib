package com.richzjc.download.notify;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;


public class NotifyUI {

    private static Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };


    public static void notifyProgress(){

    }

    public static void notifySizeChange(){

    }

    public static void notifyRequestData(){

    }

    public static void notifyStatusChange(){

    }

}
