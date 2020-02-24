package com.richzjc.download.Dispatcher

import com.richzjc.download.DOWNLOAD_PAUSE
import com.richzjc.download.RDownloadClient
import com.richzjc.download.task.ParentTask

class PauseAllDispater(val builder : RDownloadClient.Builder?){

    fun pauseAll(){
        synchronized(builder!!.running){
            builder?.running?.forEach {
                (it as? ParentTask)?.status = DOWNLOAD_PAUSE
            }
        }
    }
}