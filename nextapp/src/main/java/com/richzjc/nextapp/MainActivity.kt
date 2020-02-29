package com.richzjc.rdownload

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
    }

    fun clickTest(view: View) {}

}
