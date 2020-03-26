package com.richzjc.download

import com.richzjc.download.task.ParentTask

interface RDownload {
    fun addTask(parentTask: ParentTask?)

    fun addTask(tasks : List<ParentTask>?)

    fun pauseTask(parentTask: ParentTask?)

    fun startAll()

    fun pauseAll()

    fun getAllDownloadData() : List<ParentTask>

    fun getAllDownloadSize() : Int

    fun getRunningData() : List<ParentTask>?

    fun getPauseOrErrorData() : List<ParentTask>?

    fun deleteTask(ParentTask : ParentTask?)
}