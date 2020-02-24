package com.richzjc.download.Dispatcher

import com.richzjc.download.DOWNLOAD_PAUSE
import com.richzjc.download.RDownloadClient
import com.richzjc.download.notify.NotifyUI

class PauseAllDispater(val builder: RDownloadClient.Builder?) {

    fun pauseAll() = builder?.also {
        synchronized(builder.running) {
            builder.running?.forEach {
                it.status = DOWNLOAD_PAUSE
                builder.pauseAndError.add(it)
                NotifyUI.notifyStatusChange()
            }
            builder.running.clear()
        }
    }
}