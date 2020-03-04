package com.richzjc.download


const val LOG_TAG = "net >>>"
const val ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"

const val MAX_HOLD_DOWNLOAD_COUNT = 100

const val WAITING = 0
const val DOWNLOADING = 10
const val DOWNLOAD_PAUSE = 20
const val DOWNLOAD_FINISH = 30
const val DOWNLOAD_ERROR = 40
const val DOWNLOAD_DELETE = 50