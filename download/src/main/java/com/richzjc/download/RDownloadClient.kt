package com.richzjc.download

import com.richzjc.download.eventbus.SimpleSubscribeInfo
import com.richzjc.download.eventbus.SubscribeInfoIndex

class RDownloadClient private constructor(builder: Builder) : RDownload by RDownloadImpl(builder) {

    init {
        configs[builder.configurationKey] = this
    }

    companion object {
        val configs = HashMap<String, RDownloadClient>()
        val callbackMethods = HashMap<Class<out Any>, SimpleSubscribeInfo>()

        fun bind(configurationKey: String, obj: Any): RDownloadClient? {
            val client = configs[configurationKey]
            update(client)
            return client
        }

        fun bind(obj: Any): RDownloadClient? {
            val client = configs[""]
            update(client)
            return client
        }

        private fun update(client: RDownloadClient?) {
            //TODO 将当前的进度回调回去
        }

        fun addIndex(index : SubscribeInfoIndex?){
            index?.also {
                callbackMethods.putAll(index.subscriberInfo)
            }
        }
    }

    class Builder {
        var netWorkType: NetWorkType = NetWorkType.WIFI
        var configurationKey: String = ""
        var threadCount: Int = 1

        fun setConfigrationKey(configurationKey: String) =
            apply { this.configurationKey = configurationKey }

        fun setNetworkType(networkType: NetWorkType) = apply { this.netWorkType = networkType }

        fun setThreadCount(threadCount: Int?) = apply {
            require(!(threadCount == null || threadCount <= 0)) { "threadCount必须大于0， 意思是指同一时间有几个线程支持下载" }
            this.threadCount = threadCount
        }

        fun build(): RDownloadClient {
            return RDownloadClient(this)
        }
    }
}