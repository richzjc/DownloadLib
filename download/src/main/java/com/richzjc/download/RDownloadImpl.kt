package com.richzjc.download

import com.richzjc.download.Dispatcher.AddSingleTaskDispatcher
import com.richzjc.download.Dispatcher.PauseAllDispater
import com.richzjc.download.Dispatcher.PauseSingleTaskDispatcher
import com.richzjc.download.Dispatcher.StartAllDispater
import com.richzjc.download.task.IParentTask

class RDownloadImpl(val client : RDownloadClient.Builder) : RDownload {

    private val pauseAllDispater by lazy { PauseAllDispater(client) }
    private val startAllDispater by lazy { StartAllDispater(client) }
    private val startSingleDispatcher by lazy { AddSingleTaskDispatcher(client) }
    private val pauseSingleDispatcher by lazy { PauseSingleTaskDispatcher(client) }

    override fun addTask(parentTask: IParentTask?) = startSingleDispatcher?.addTask(parentTask)

    override fun pauseTask(parentTask: IParentTask?)  = pauseSingleDispatcher?.pauseSingleTask(parentTask)

    override fun startAll() = startAllDispater?.startAll()

    override fun pauseAll() = pauseAllDispater?.pauseAll()

    override fun getAllDownloadData() = ArrayList<IParentTask>().apply {
        addAll(client.running)
        addAll(client.pauseAndError)
    }

    override fun getAllDownloadSize() = client.running.size + client.pauseAndError.size
}