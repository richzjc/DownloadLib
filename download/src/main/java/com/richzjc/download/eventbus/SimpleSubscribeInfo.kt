package com.richzjc.download.eventbus

class SimpleSubscribeInfo(val sizeChangeMethod : List<SubscribeMethod>?,
                          val pauseAllMethod : List<SubscribeMethod>?,
                          val startAllMethod : List<SubscribeMethod>?,
                          val pauseStart : List<SubscribeMethod>?,
                          val pauseStartEmpty : List<SubscribeMethod>?,
                          val netChangeMethod : List<SubscribeMethod>?) : SubscribeInfo {

    override fun sizeChangeMethod(): List<SubscribeMethod>? = sizeChangeMethod

    override fun startAllMethod(): List<SubscribeMethod>? = startAllMethod

    override fun pauseAllMethod(): List<SubscribeMethod>? = pauseAllMethod

    override fun pauseStartMethods(): List<SubscribeMethod>? = pauseStart

    override fun pauseStartEmptyMethods(): List<SubscribeMethod>? = pauseStartEmpty

    override fun netChangeMethods(): List<SubscribeMethod>?  = netChangeMethod
}