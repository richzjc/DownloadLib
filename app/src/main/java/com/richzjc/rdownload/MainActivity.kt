package com.richzjc.rdownload

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.richzjc.download.RDownloadClient
import com.richzjc.downloadannotation.ProgressChange
import com.richzjc.downloadannotation.SizeChange

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SizeChange
    fun onSizeChange(){

    }

    @ProgressChange
    fun progressChange(){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun clickTest(view : View){
        RDownloadClient.bind(this)?.addTask(DownloadTask())
    }
}
