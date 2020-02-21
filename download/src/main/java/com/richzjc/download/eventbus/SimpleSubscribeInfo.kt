package com.richzjc.download.eventbus

import java.lang.reflect.Method

class SimpleSubscribeInfo(val sizeChangeMethod : List<Method>?, val progressChangeMethod : List<Method>?, val requestDataMethod : List<Method>?) : SubscribeInfo {

    override fun sizeChangeMethod(): List<Method>? = sizeChangeMethod

    override fun requestDataMethod(): List<Method>? = requestDataMethod

    override fun progressChangeMethod(): List<Method>? = progressChangeMethod
}