package com.richzjc.download.Dispatcher

import com.richzjc.download.DOWNLOADING
import com.richzjc.download.RDownloadClient
import com.richzjc.download.WAITING
import com.richzjc.download.task.ParentTask

class AddSingleTaskDispatcher(val builder : RDownloadClient.Builder?) {

    fun addTask(parentTask : ParentTask?) = parentTask?.also {
        if(it.status == WAITING || it.status == DOWNLOADING){
            builder?.running?.add(it)
            builder?.okHttpClient?.dispatcher?.executorService?.execute(it)
        }else{
           builder?.pauseAndError?.add(it)
        }
    }
}