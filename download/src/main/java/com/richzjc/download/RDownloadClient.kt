package com.richzjc.download

import com.richzjc.download.eventbus.SimpleSubscribeInfo
import com.richzjc.download.eventbus.SubscribeInfoIndex

class RDownloadClient private constructor(builder: Builder) : RDownload by RDownloadImpl(builder) {

    init {
        configs[builder.configurationKey] = this
    }

    companion object {
        private val configs = HashMap<String, RDownloadClient>()
        private val callbackMethods = HashMap<Class<out Any>, SimpleSubscribeInfo>()
        private val subscribeInfos = HashMap<String,  HashMap<Any, SimpleSubscribeInfo?>>()


        fun bind(configurationKey: String, obj: Any): RDownloadClient? {
            return update(configurationKey, obj)
        }

        fun bind(obj: Any): RDownloadClient? {
            return update("", obj)
        }

        private fun update(configurationKey: String, obj: Any) : RDownloadClient?{
            //TODO 将当前的进度回调回去
            val client = configs[configurationKey]
            if(!subscribeInfos.containsKey(configurationKey)){
                val map = HashMap<Any, SimpleSubscribeInfo?>()
                subscribeInfos.put(configurationKey, map)
            }
            val map = subscribeInfos[configurationKey]
            val simpleSubscribeInfo = map?.get(obj)
            simpleSubscribeInfo?.also {
                //TODO 把相关的进度回调回去
            }
            return client
        }

        fun addIndex(index : SubscribeInfoIndex?){
            index?.also {
                callbackMethods.putAll(index.subscriberInfo)
            }
        }

        fun unBind(configurationKey: String, obj : Any){

        }

        fun unBind(obj : Any){
            unBind("", obj)
        }
    }

    class Builder {
        var netWorkType: NetWorkType = NetWorkType.WIFI
        var configurationKey: String = ""
        var threadCount: Int = 1
        var maxDownloadCount : Int = MAX_HOLD_DOWNLOAD_COUNT

        fun setMaxDownloadCount(maxCount : Int) = apply {
            require(!(maxCount == null || maxCount <= 0)) { "maxCount必须大于0， 意思是指最多只能添加多少个下载任务" }
            this.maxDownloadCount = maxCount
        }

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