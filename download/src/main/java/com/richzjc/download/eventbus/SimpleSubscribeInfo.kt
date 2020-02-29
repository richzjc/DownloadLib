package com.richzjc.download.eventbus

class SimpleSubscribeInfo(val sizeChangeMethod : List<SubscribeMethod>?, val pauseAllMethod : List<SubscribeMethod>?, val startAllMethod : List<SubscribeMethod>?) : SubscribeInfo {

    override fun sizeChangeMethod(): List<SubscribeMethod>? = sizeChangeMethod

    override fun startAllMethod(): List<SubscribeMethod>? = startAllMethod

    override fun pauseAllMethod(): List<SubscribeMethod>? = pauseAllMethod
}