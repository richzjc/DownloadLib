package com.richzjc.download.okhttp

import android.util.Log
import com.richzjc.download.DOWNLOADING
import com.richzjc.download.DOWNLOAD_DELETE
import com.richzjc.download.RDownloadClient
import com.richzjc.download.WAITING
import com.richzjc.download.task.ParentTask
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor

class CustomRejectHander(val builder: RDownloadClient.Builder?) : RejectedExecutionHandler {

    override fun rejectedExecution(r: Runnable, executor: ThreadPoolExecutor) {
        synchronized(builder!!) {
            (r as? ParentTask)?.also {
                if(it.status == DOWNLOAD_DELETE){
                    builder.running.remove(it)
                    builder.pauseAndError.remove(it)
                }else if (it.status == WAITING || it.status == DOWNLOADING) {
                    builder?.pauseAndError?.remove(it)
                    if (!builder.running.contains(it))
                        builder?.running?.add(it)
                } else if (!builder.pauseAndError.contains(it)) {
                    builder?.running?.remove(it)
                    builder?.pauseAndError?.add(it)
                }
                Log.i("download", "reject : " + r.toString());
            }
        }
    }
}
