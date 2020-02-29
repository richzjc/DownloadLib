package com.richzjc.download.Dispatcher

import com.richzjc.download.DOWNLOADING
import com.richzjc.download.DOWNLOAD_DELETE
import com.richzjc.download.RDownloadClient
import com.richzjc.download.WAITING
import com.richzjc.download.notify.NotifyUI
import com.richzjc.download.task.ParentTask

class AddSingleTaskDispatcher(val builder: RDownloadClient.Builder?) {

    fun addTask(parentTask: ParentTask?) = builder?.also {
        synchronized(builder){
            parentTask?.also {
                if (it.status == WAITING || it.status == DOWNLOADING) {
                    builder?.running?.add(it)
                    builder?.okHttpClient?.dispatcher?.executorService?.execute(it)
                } else if(it.status == DOWNLOAD_DELETE){
                    builder?.running?.remove(it)
                    builder?.pauseAndError?.remove(it)
                }else {
                    builder?.running?.remove(it)
                    builder?.pauseAndError?.add(it)
                }
                NotifyUI.notifyAllSizeChange(builder.configurationKey)
            }
        }
    }
}