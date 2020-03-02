package com.richzjc.download.Dispatcher

import com.richzjc.download.DOWNLOADING
import com.richzjc.download.DOWNLOAD_DELETE
import com.richzjc.download.RDownloadClient
import com.richzjc.download.WAITING
import com.richzjc.download.notify.NotifyUI
import com.richzjc.download.task.ParentTask

class DeleteSingleDispatcher(val builder: RDownloadClient.Builder?) {
    fun deleteSingle(task: ParentTask?) {
        builder?.also {
            synchronized(builder) {
                task?.also {
                    it.status = DOWNLOAD_DELETE
                    builder.running?.remove(it)
                    builder.pauseAndError?.remove(it)
                    NotifyUI.notifyAllSizeChange(builder.configurationKey)
                }
            }
        }
    }
}