package com.richzjc.download.task

import com.richzjc.download.RDownloadClient

interface IChildTask {
    fun run(builder : RDownloadClient.Builder, parentTask : ParentTask?) : Boolean
}