package com.richzjc.download

import com.richzjc.download.Dispatcher.PauseAllDispater
import com.richzjc.download.Dispatcher.StartAllDispater
import com.richzjc.download.task.IParentTask

class RDownloadImpl(val client : RDownloadClient.Builder) : RDownload {

    private val pauseAllDispater by lazy { PauseAllDispater(client) }
    private val startAllDispater by lazy { StartAllDispater(client) }

    override fun addTask(parentTask: IParentTask?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    override fun pauseTask(parentTask: IParentTask?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startAll() = startAllDispater?.startAll()

    override fun pauseAll() = pauseAllDispater?.pauseAll()

    override fun getAllDownloadData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllDownloadSize() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}