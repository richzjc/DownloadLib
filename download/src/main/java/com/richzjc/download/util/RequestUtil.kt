package com.richzjc.download.util

import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.richzjc.download.DOWNLOADING
import com.richzjc.download.RDownloadClient
import com.richzjc.download.okhttp.GET
import com.richzjc.download.okhttp.HEAD
import com.richzjc.download.okhttp.IRequestParamter
import com.richzjc.download.okhttp.POST
import com.richzjc.download.task.ChildTask
import com.richzjc.download.task.ParentTask
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.BufferedInputStream
import java.io.File
import java.io.RandomAccessFile

fun request(okHttpClient: OkHttpClient?, params: IRequestParamter?): Boolean {
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
        if (!response.isSuccessful)
            return false

        val result = response.body?.string()
        if (TextUtils.isEmpty(params.getResultJSONKey())) {
            val obj = JSON.parseObject(result, params.javaClass)
            updateParamsAttr(params, obj)
        } else {
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

fun requestLength(okHttpClient: OkHttpClient, r: ParentTask): Boolean {
    if (r.childTasks == null || r.childTasks.isEmpty() || r.totalLength > 0)
        return true

    var isSuccess = true
    var totalLength = 0L
    var request: Request? = null
    r.childTasks?.forEach {
        if (r.status == DOWNLOADING) {
            request = Request.Builder().url(it.requestUrl).head().build()
            val response = okHttpClient.newCall(request!!).execute()
            if (response != null && response.isSuccessful) {
                val length = response.header("content-length", "0");
                it.totalLenght = length?.toLong() ?: 0L
                totalLength += it.totalLenght
            } else {
                isSuccess = false
                return@forEach
            }
        } else {
            isSuccess = false
            return@forEach
        }
    }
    if (isSuccess)
        r.totalLength = totalLength
    return isSuccess
}


fun download(builder: RDownloadClient.Builder?, parentTask: ParentTask, task: ChildTask?): Boolean {
    var filePath: String? = ""
    if (TextUtils.isEmpty(task?.filePath)) {
        filePath = DownloadUtil.getDownloadFilePath(builder, task)
        task?.filePath = filePath
    } else {
        filePath = task?.filePath
    }

    Log.i("file", filePath)
    val file = File(filePath)
    if (!FileUtil.createFile(file.absolutePath))
        return false

    if(task!!.totalLenght > 0 && file.length() ==  task!!.totalLenght)
        return true

    val range = "bytes=" + file.length() + "-"
    val request = Request.Builder().url(task!!.requestUrl)
        .addHeader("RANGE", range)
        .addHeader("Connection", "close")
        .build()
    try {
        val response = builder?.okHttpClient?.newCall(request)?.execute()
        if (response != null && response.isSuccessful) {
            val body = response.body
            Log.i("body", body!!.contentType().toString())
            return writeFiles(parentTask, task, body, file)
        } else {
            return false;
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

private fun writeFiles(
    parentTask: ParentTask,
    childTask: ChildTask,
    body: ResponseBody,
    file: File
): Boolean {
    try {
        val accessFile = RandomAccessFile(file, "rw")
        accessFile.seek(0)
        var readCount: Int
        val len = 1024
        val buffer = ByteArray(len)
        val bufferInputStream = BufferedInputStream(body.byteStream(), len)
        var isSuccess = true
        do{
            readCount = bufferInputStream.read(buffer)
            if(readCount != -1){
                if(parentTask.checkCanDownload()){
                    accessFile.write(buffer, 0, readCount)
                    parentTask.downloadLength = parentTask.downloadLength + readCount
                    childTask.downloadLenght = childTask.downloadLenght + readCount
                }else{
                    isSuccess = false
                    break
                }
            }else{
                break
            }
        }while (true)
        accessFile.close()
        bufferInputStream.close()
        return isSuccess
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}