package com.richzjc.download.Dispatcher

import com.richzjc.download.DOWNLOAD_PAUSE
import com.richzjc.download.RDownloadClient
import com.richzjc.download.task.ParentTask

class PauseSingleTaskDispatcher(val builder : RDownloadClient.Builder?) {

    fun pauseSingleTask(task : ParentTask?) = builder?.also {
        synchronized(builder){
            task?.status = DOWNLOAD_PAUSE
            builder.running.remove(task)
            task?.also { builder.pauseAndError.add(it) }
        }
    }
}