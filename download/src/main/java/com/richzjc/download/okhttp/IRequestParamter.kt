package com.richzjc.download.okhttp


interface IRequestParamter {
    fun getRequestUrl() : String?
    fun getRequestMethod() : Int?
    fun getRequestHeader() : Map<String, String>?
    fun getResultJSONKey() : String?
}