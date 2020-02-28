package com.richzjc.download.okhttp

import com.richzjc.download.DOWNLOADING
import com.richzjc.download.RDownloadClient
import com.richzjc.download.WAITING
import com.richzjc.download.task.ParentTask
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor

class CustomRejectHander(val builder: RDownloadClient.Builder?) : RejectedExecutionHandler {

    override fun rejectedExecution(r: Runnable, executor: ThreadPoolExecutor) {
        (r as? ParentTask)?.also {
            synchronized(builder!!) {
                builder?.running?.remove(it)
                builder?.pauseAndError?.remove(it)
                if (it.status == WAITING || it.status == DOWNLOADING) {
                    builder?.running?.add(it)
                } else {
                    builder?.pauseAndError?.add(it)
                }
            }
        }
    }
}
