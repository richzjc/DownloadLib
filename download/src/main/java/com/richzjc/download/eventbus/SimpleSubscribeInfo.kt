package com.richzjc.download.eventbus

class SimpleSubscribeInfo(val sizeChangeMethod : List<SubscribeMethod>?, val progressChangeMethod : List<SubscribeMethod>?, val requestDataMethod : List<SubscribeMethod>?) : SubscribeInfo {

    override fun sizeChangeMethod(): List<SubscribeMethod>? = sizeChangeMethod

    override fun requestDataMethod(): List<SubscribeMethod>? = requestDataMethod

    override fun progressChangeMethod(): List<SubscribeMethod>? = progressChangeMethod
}