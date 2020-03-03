package com.richzjc.download

import android.content.Context
import com.richzjc.download.eventbus.SimpleSubscribeInfo
import com.richzjc.download.eventbus.SubscribeInfoIndex
import com.richzjc.download.notify.NotifyUI
import com.richzjc.download.okhttp.CustomRejectHander
import com.richzjc.download.okhttp.CustomThreadPoolExecutor
import com.richzjc.download.task.ParentTask
import com.richzjc.download.util.RLinkdList
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.internal.threadFactory
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

class RDownloadClient private constructor(val builder: Builder) :
    RDownload by RDownloadImpl(builder) {
    init {
        configs[builder.configurationKey] = this
    }

    companion object {
        private val configs = HashMap<String, RDownloadClient>()
        val subscribeInfos = HashMap<String, HashMap<Any, SimpleSubscribeInfo?>>()
        val callbackMethods = HashMap<Class<out Any>, SimpleSubscribeInfo>()

        fun getClient(configurationKey: String?): RDownloadClient? {
            configurationKey ?: return null
            return configs[configurationKey]
        }

        fun bind(configurationKey: String, obj: Any): RDownloadClient? {
            return update(configurationKey, obj)
        }

        fun bind(obj: Any): RDownloadClient? {
            return update("", obj)
        }

        private fun update(configurationKey: String, obj: Any): RDownloadClient? {
            val client = configs[configurationKey]
            if (!subscribeInfos.containsKey(configurationKey)) {
                val map = HashMap<Any, SimpleSubscribeInfo?>()
                subscribeInfos.put(configurationKey, map)
            }
            val map = subscribeInfos[configurationKey]
            if (!map!!.containsKey(obj) && callbackMethods.containsKey(obj::class.java)) {
                map[obj] = callbackMethods[obj::class.java]
            }
            NotifyUI.notifySigleChange(obj, configs[configurationKey])
            return client
        }

        fun addIndex(index: SubscribeInfoIndex?) {
            index?.also {
                callbackMethods.putAll(index.subscriberInfo)
            }
        }

        fun unBind(configurationKey: String, obj: Any) {
            val map = subscribeInfos.get(configurationKey)
            map?.also {
                map.remove(obj)
            }
        }

        fun unBind(obj: Any) {
            unBind("", obj)
        }
    }

    class Builder {
        var netWorkType: NetWorkType = NetWorkType.WIFI
            private set
        var configurationKey: String = ""
            private set
        var threadCount: Int = 1
            private set
        var maxDownloadCount: Int = MAX_HOLD_DOWNLOAD_COUNT
            private set
        var okHttpClient: OkHttpClient? = null
            private set
        var filePath: String? = null
            private set
        var context: Context? = null
            private set

        val running = RLinkdList<ParentTask>(this)
        val pauseAndError = RLinkdList<ParentTask>(this)

        fun setMaxDownloadCount(maxCount: Int) = apply {
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

        fun setDownloadFilePath(filePath: String?) {
            this.filePath = filePath
        }

        fun build(context : Context?): RDownloadClient {
            this.context = context?.applicationContext
            val okHttpName =
                OkHttpClient::class.java.name.removePrefix("okhttp3.").removeSuffix("Client")

            okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .dispatcher(
                    Dispatcher(
                        CustomThreadPoolExecutor(
                            0, threadCount, 60, TimeUnit.SECONDS,
                            SynchronousQueue(), threadFactory("$okHttpName Dispatcher", false)
                            , CustomRejectHander(this)
                        )
                    )
                )
                .build()
            (okHttpClient?.dispatcher?.executorService as? CustomThreadPoolExecutor)?.builder = this
            okHttpClient?.dispatcher?.maxRequests = threadCount
            return RDownloadClient(this)
        }
    }
}