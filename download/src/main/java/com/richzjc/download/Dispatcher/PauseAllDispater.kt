package com.richzjc.download.Dispatcher

import com.richzjc.download.DOWNLOAD_PAUSE
import com.richzjc.download.RDownloadClient
import com.richzjc.download.notify.NotifyUI
import com.richzjc.download.task.ParentTask

class PauseAllDispater(val builder: RDownloadClient.Builder?) {

    fun pauseAll() = builder?.also {
        synchronized(builder) {
            builder.running?.forEach {
                it.status = DOWNLOAD_PAUSE
                builder.pauseAndError.add(it)
                NotifyUI.notifyStatusChange(builder, it)
            }
            builder.running.clear()
        }
    }
}