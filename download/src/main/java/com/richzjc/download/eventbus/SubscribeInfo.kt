package com.richzjc.download.eventbus


interface SubscribeInfo {
    fun sizeChangeMethod() : List<SubscribeMethod>?
    fun startAllMethod() : List<SubscribeMethod>?
    fun pauseAllMethod() : List<SubscribeMethod>?
}