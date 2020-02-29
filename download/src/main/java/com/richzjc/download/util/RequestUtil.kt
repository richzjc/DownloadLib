package com.richzjc.download.util

import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.richzjc.download.okhttp.GET
import com.richzjc.download.okhttp.HEAD
import com.richzjc.download.okhttp.IRequestParamter
import com.richzjc.download.okhttp.POST
import okhttp3.OkHttpClient
import okhttp3.Request

fun request(okHttpClient: OkHttpClient?, params: IRequestParamter?) : Boolean{
    okHttpClient ?: return false;
    params ?: return false;

    val builder = Request.Builder().url(params!!.getRequestUrl() ?: "")
    when (params.getRequestMethod()) {
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
    try {
        val response = call.execute()
        val result = response.body?.string()
        if(TextUtils.isEmpty(params.getResultJSONKey())) {
            val obj = JSON.parseObject(result, params.javaClass)
            updateParamsAttr(params, obj)
        }else{
            val jobj = org.json.JSONObject(result)
            val obj = JSON.parseObject(jobj.optString(params.getResultJSONKey()), params.javaClass)
            updateParamsAttr(params, obj)
        }
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

fun updateParamsAttr(origin: IRequestParamter, current: IRequestParamter?) {
    val list = FieldUtils.getFields(origin.javaClass)
    list?.forEach { outer ->
        outer.isAccessible = true
        val any = outer.get(current)
        any?.also { outer.set(origin, any) }
    }
}
