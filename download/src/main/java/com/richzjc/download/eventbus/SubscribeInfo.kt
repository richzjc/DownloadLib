package com.richzjc.download.eventbus

import java.lang.reflect.Method

interface SubscribeInfo {
    fun sizeChangeMethod() : Method?
    fun requestDataMethod() : Method?
    fun progressChangeMethod() : Method?
}