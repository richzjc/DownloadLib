package com.richzjc.download.Dispatcher

import com.richzjc.download.RDownloadClient
import com.richzjc.download.WAITING

class StartAllDispater(val builder : RDownloadClient.Builder?){

    fun startAll() =  builder?.also{outer ->
        synchronized(outer){
            val list = ArrayList(outer.pauseAndError)
            outer.pauseAndError.clear()
            list?.forEach {
                it.status = WAITING
                builder.running.add(it)
                builder.okHttpClient?.dispatcher?.executorService?.execute(it)
            }
        }
    }
}