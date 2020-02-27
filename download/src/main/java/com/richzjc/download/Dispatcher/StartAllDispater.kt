package com.richzjc.download.Dispatcher

import com.richzjc.download.RDownloadClient
import com.richzjc.download.WAITING
import com.richzjc.download.notify.NotifyUI

class StartAllDispater(val builder : RDownloadClient.Builder?){

    fun startAll() =  builder?.also{
        synchronized(it){
            it.pauseAndError?.forEach {
                it.status = WAITING
                builder.running.add(it)
                builder.okHttpClient?.dispatcher?.executorService?.execute(it)
                NotifyUI.notifyStatusChange();
            }
            it.pauseAndError.clear()
        }
    }
}