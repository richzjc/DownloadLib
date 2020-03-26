package com.richzjc.download

import com.richzjc.download.Dispatcher.*
import com.richzjc.download.task.ParentTask
import java.util.concurrent.Executors

class RDownloadImpl(val client: RDownloadClient.Builder) : RDownload {

    private val pauseAllDispater by lazy { PauseAllDispater(client) }
    private val startAllDispater by lazy { StartAllDispater(client, startSingleDispatcher) }
    private val startSingleDispatcher by lazy { AddSingleTaskDispatcher(client) }
    private val pauseSingleDispatcher by lazy { PauseSingleTaskDispatcher(client) }
    private val deleteSingleDispatcher by lazy { DeleteSingleDispatcher(client) }

    private val executorService by lazy {
        Executors.newFixedThreadPool(1)
    }


    override fun addTask(parentTask: ParentTask?) {
        executorService.execute {
            startSingleDispatcher?.addTask(parentTask)
        }
    }

    override fun addTask(tasks: List<ParentTask>?) {
        executorService.execute {
            startSingleDispatcher?.addTasks(tasks)
        }
    }

    override fun pauseTask(parentTask: ParentTask?) {
        executorService.execute {
            pauseSingleDispatcher?.pauseSingleTask(parentTask)
        }
    }

    override fun startAll() {
        executorService.execute {
            startAllDispater?.startAll()
        }
    }

    override fun pauseAll() {
        executorService.execute {
            pauseAllDispater?.pauseAll()
        }
    }

    override fun getAllDownloadData() = ArrayList<ParentTask>().apply {
        addAll(client.running)
        addAll(client.pauseAndError)
    }

    override fun getAllDownloadSize() = client.running.size + client.pauseAndError.size

    override fun getRunningData(): List<ParentTask>? = client.running

    override fun getPauseOrErrorData(): List<ParentTask>? = client.pauseAndError

    override fun deleteTask(parentTask: ParentTask?) {
        executorService.execute {
            deleteSingleDispatcher?.deleteSingle(parentTask)
        }
    }
}