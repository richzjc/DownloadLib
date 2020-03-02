package com.richzjc.download

import com.richzjc.download.Dispatcher.*
import com.richzjc.download.task.ParentTask

class RDownloadImpl(val client: RDownloadClient.Builder) : RDownload {

    private val pauseAllDispater by lazy { PauseAllDispater(client) }
    private val startAllDispater by lazy { StartAllDispater(client) }
    private val startSingleDispatcher by lazy { AddSingleTaskDispatcher(client) }
    private val pauseSingleDispatcher by lazy { PauseSingleTaskDispatcher(client) }
    private val deleteSingleDispatcher by lazy { DeleteSingleDispatcher(client) }

    override fun addTask(parentTask: ParentTask?) {
        startSingleDispatcher?.addTask(parentTask)
    }

    override fun pauseTask(parentTask: ParentTask?) {
        pauseSingleDispatcher?.pauseSingleTask(parentTask)
    }

    override fun startAll() {
        startAllDispater?.startAll()
    }

    override fun pauseAll() {
        pauseAllDispater?.pauseAll()
    }

    override fun getAllDownloadData() = ArrayList<ParentTask>().apply {
        addAll(client.running)
        addAll(client.pauseAndError)
    }

    override fun getAllDownloadSize() = client.running.size + client.pauseAndError.size

    override fun getRunningData(): List<ParentTask>?  = client.running

    override fun getPauseOrErrorData(): List<ParentTask>?  = client.pauseAndError

    override fun deleteTask(parentTask: ParentTask?){
        deleteSingleDispatcher?.deleteSingle(parentTask)
    }
}