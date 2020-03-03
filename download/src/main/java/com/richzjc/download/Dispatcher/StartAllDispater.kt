package com.richzjc.download.Dispatcher

import com.richzjc.download.RDownloadClient

class StartAllDispater(val builder : RDownloadClient.Builder?, val singleDispatch : AddSingleTaskDispatcher?){

    fun startAll() =  builder?.also{outer ->
        synchronized(outer){
            val list = ArrayList(outer.pauseAndError)
            outer.pauseAndError.clear()
            list?.forEach {
               singleDispatch?.addTask(it)
            }
        }
    }
}