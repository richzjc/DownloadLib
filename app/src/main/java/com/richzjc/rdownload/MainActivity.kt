package com.richzjc.rdownload

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.richzjc.download.NetWorkType
import com.richzjc.download.RDownloadClient
import com.richzjc.downloadannotation.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    @SizeChange
    fun onSizeChange(position : Int){
        downloadCount?.text = "总共在下载的数量是： ${position}"
    }

    @PauseAll
    fun progressChange(){
        pauseOrStart?.text = "已经全部暂停了"
    }

    @StartAll
    fun startAll(){
        pauseOrStart?.text = "已经全部在下载了"
    }

    @PauseAndStart
    fun pauseStart(){
        pauseOrStart?.text = "有暂停的 也有下载的"
    }

    @NetChange
    fun onNetChange(type : NetWorkType?){
        Toast.makeText(this, "" + type, Toast.LENGTH_LONG).show()
    }

    @PauseStartEmpty
    fun onPauseStartEmpty(){

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
