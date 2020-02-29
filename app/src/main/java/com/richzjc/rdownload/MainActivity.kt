package com.richzjc.rdownload

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.richzjc.download.RDownloadClient
import com.richzjc.downloadannotation.PauseAll
import com.richzjc.downloadannotation.SizeChange
import com.richzjc.downloadannotation.StartAll
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    @SizeChange
    fun onSizeChange(position : Int){
        downloadCount?.text = "总共在下载的数量是： ${position}"
    }

    @PauseAll
    fun progressChange(){

    }

    @StartAll
    fun startAll(){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RDownloadClient.bind(this)
    }

    fun clickTest(view : View){
        startActivity(Intent(this, DonwloadDetailActivity::class.java))
    }
}
