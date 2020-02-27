package com.richzjc.download.okhttp

import okhttp3.RequestBody

interface IRequestParamter {
    fun getRequestUrl() : String?
    fun getRequestMethod() : Int?
    fun getRequestHeader() : Map<String, String>?
    fun getRequestBody() : RequestBody?
}