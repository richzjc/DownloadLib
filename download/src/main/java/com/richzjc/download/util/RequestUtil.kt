package com.richzjc.download.util

import android.util.Log
import com.richzjc.download.okhttp.GET
import com.richzjc.download.okhttp.HEAD
import com.richzjc.download.okhttp.IRequestParamter
import com.richzjc.download.okhttp.POST
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

fun request(okHttpClient: OkHttpClient?, params : IRequestParamter?){
    okHttpClient ?: return
    params ?: return

    val builder = Request.Builder().url(params!!.getRequestUrl() ?: "")
    when(params.getRequestMethod()){
        GET -> builder.method("GET", params.getRequestBody())
        POST -> builder.method("POST", params.getRequestBody())
        HEAD -> builder.method("HEAD", params.getRequestBody())
        else -> builder.method("GET", params.getRequestBody())
    }

    params.getRequestHeader()?.forEach {
        builder.addHeader(it.key, it.value)
    }

    val call = okHttpClient.newCall(builder.build())
    Log.i("thread", "request: ${Thread.currentThread().name}")
    val response = call.execute()
    Log.i("result", response.body.toString())
    val jobj = JSONObject(response.body.toString())

}