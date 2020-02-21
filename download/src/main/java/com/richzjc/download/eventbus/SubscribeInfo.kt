package com.richzjc.download.eventbus

import java.lang.reflect.Method

interface SubscribeInfo {
    fun sizeChangeMethod() : List<Method>?
    fun requestDataMethod() : List<Method>?
    fun progressChangeMethod() : List<Method>?
}