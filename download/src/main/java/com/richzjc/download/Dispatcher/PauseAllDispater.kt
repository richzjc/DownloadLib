package com.richzjc.download.Dispatcher

import com.richzjc.download.DOWNLOAD_PAUSE
import com.richzjc.download.RDownloadClient

class PauseAllDispater(val builder: RDownloadClient.Builder?) {

    fun pauseAll() = builder?.also {
        synchronized(builder) {
            builder.running?.forEach {
                it.status = DOWNLOAD_PAUSE
                builder.pauseAndError.add(it)
            }
            builder.running.clear()
        }
    }
}