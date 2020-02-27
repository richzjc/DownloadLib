package com.richzjc.download.util

import android.util.Log
import com.alibaba.fastjson.JSON
import com.richzjc.download.okhttp.GET
import com.richzjc.download.okhttp.HEAD
import com.richzjc.download.okhttp.IRequestParamter
import com.richzjc.download.okhttp.POST
import okhttp3.OkHttpClient
import okhttp3.Request

fun request(okHttpClient: OkHttpClient?, params : IRequestParamter?){
    okHttpClient ?: return
    params ?: return

    val builder = Request.Builder().url(params!!.getRequestUrl() ?: "")
    when(params.getRequestMethod()){
        GET -> builder.method("GET", null)
        POST -> builder.method("POST", null)
        HEAD -> builder.method("HEAD", null)
        else -> builder.method("GET", null)
    }

    params.getRequestHeader()?.forEach {
        builder.addHeader(it.key, it.value)
    }

    val call = okHttpClient.newCall(builder.build())
    Log.i("thread", "request: ${Thread.currentThread().name}")
    val response = call.execute()
    Log.i("result", response.body?.string())
    try {
        val obj = JSON.parseObject(response.body?.string(), params.javaClass)
        updateParamsAttr(params, obj)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

fun updateParamsAttr(origin: IRequestParamter, current: IRequestParamter?) {

}
