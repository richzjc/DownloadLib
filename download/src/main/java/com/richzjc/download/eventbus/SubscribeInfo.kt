package com.richzjc.download.eventbus


interface SubscribeInfo {
    fun sizeChangeMethod() : List<SubscribeMethod>?
    fun startAllMethod() : List<SubscribeMethod>?
    fun pauseAllMethod() : List<SubscribeMethod>?
    fun pauseStartMethods() : List<SubscribeMethod>?
    fun pauseStartEmptyMethods() : List<SubscribeMethod>?
    fun netChangeMethods() : List<SubscribeMethod>?
}