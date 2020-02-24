package com.richzjc.download

import com.richzjc.download.task.ParentTask

interface RDownload {
    fun addTask(parentTask: ParentTask?)
    fun pauseTask(parentTask: ParentTask?)

    fun startAll()

    fun pauseAll()


    fun getAllDownloadData() : List<ParentTask>

    fun getAllDownloadSize() : Int


}