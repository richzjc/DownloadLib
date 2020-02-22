package com.richzjc.rdownload

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.richzjc.downloadannotation.ProgressChange
import com.richzjc.downloadannotation.SizeChange


class MainActivity : AppCompatActivity() {

    @SizeChange
    fun onSizeChange(){

    }

    @ProgressChange
    fun progressChange(){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}
