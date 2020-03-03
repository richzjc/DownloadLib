package com.richzjc.download.Dispatcher

import com.richzjc.download.DOWNLOAD_PAUSE
import com.richzjc.download.RDownloadClient

class PauseAllDispater(val builder: RDownloadClient.Builder?) {

    fun pauseAll() = builder?.also {
        synchronized(builder) {
            val list = ArrayList(builder.running)
            builder.running.clear()
            list?.forEach {
                it.status = DOWNLOAD_PAUSE
                builder.pauseAndError.add(it)
            }
        }
    }
}