package com.richzjc.download

import com.richzjc.download.task.IParentTask

interface RDownload {
    fun addTask(parentTask: IParentTask?)
    fun pauseTask(parentTask: IParentTask?)

    fun startAll()

    fun pauseAll()


    fun getAllDownloadData()

    fun getAllDownloadSize()


}