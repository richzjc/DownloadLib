package com.richzjc.download.eventbus


interface SubscribeInfo {
    fun sizeChangeMethod() : List<SubscribeMethod>?
    fun requestDataMethod() : List<SubscribeMethod>?
    fun progressChangeMethod() : List<SubscribeMethod>?
}