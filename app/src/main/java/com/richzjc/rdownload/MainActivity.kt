package com.richzjc.rdownload

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.richzjc.downloadannotation.PauseAll
import com.richzjc.downloadannotation.SizeChange

class MainActivity : AppCompatActivity() {

    @SizeChange
    fun onSizeChange(){

    }

    @PauseAll
    fun progressChange(){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun clickTest(view : View){
        startActivity(Intent(this, DonwloadDetailActivity::class.java))
    }
}
