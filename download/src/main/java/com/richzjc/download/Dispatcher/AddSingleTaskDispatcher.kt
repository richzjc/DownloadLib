package com.richzjc.download.Dispatcher

import com.richzjc.download.DOWNLOAD_DELETE
import com.richzjc.download.RDownloadClient
import com.richzjc.download.WAITING
import com.richzjc.download.notify.NotifyUI
import com.richzjc.download.task.ParentTask

class AddSingleTaskDispatcher(val builder: RDownloadClient.Builder?) {

    fun addTask(parentTask: ParentTask?) = builder?.also {
        synchronized(builder) {
            if ((builder.running.size + builder.pauseAndError.size) < builder.maxDownloadCount) {
                parentTask?.also {
                    if (it.status == DOWNLOAD_DELETE) {
                        val flag = builder?.running?.remove(it)
                        val flag1 = builder?.pauseAndError?.remove(it)
                        if (flag || flag1)
                            NotifyUI.notifyAllSizeChange(builder.configurationKey)
                    } else {
                        it.status = WAITING
                        val flag = builder.pauseAndError.remove(it)
                        val flag1 = builder.running.remove(it)
                        builder?.running?.add(it)
                        builder?.okHttpClient?.dispatcher?.executorService?.execute(it)
                        if (!flag && !flag1) {
                            NotifyUI.notifyAllSizeChange(builder.configurationKey)
                        }
                    }
                }

            }
        }
    }

    fun addTasks(tasks: List<ParentTask>?) = builder?.also {
        synchronized(builder) {
            tasks?.forEach {
                if ((builder.running.size + builder.pauseAndError.size) < builder.maxDownloadCount) {
                    it?.also {
                        if (it.status == DOWNLOAD_DELETE) {
                            val flag = builder?.running?.remove(it)
                            val flag1 = builder?.pauseAndError?.remove(it)
                            if (flag || flag1)
                                NotifyUI.notifyAllSizeChange(builder.configurationKey)
                        } else {
                            it.status = WAITING
                            val flag = builder.pauseAndError.remove(it)
                            val flag1 = builder.running.remove(it)
                            builder?.running?.add(it)
                            builder?.okHttpClient?.dispatcher?.executorService?.execute(it)
                            if (!flag && !flag1) {
                                NotifyUI.notifyAllSizeChange(builder.configurationKey)
                            }
                        }
                    }
                }
            }
        }
    }
}