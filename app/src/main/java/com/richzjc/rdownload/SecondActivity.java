package com.richzjc.rdownload;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.richzjc.downloadannotation.ProgressChange;
import com.richzjc.downloadannotation.RequestDataSucc;
import com.richzjc.downloadannotation.SizeChange;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SizeChange
    void onSizeChange(){

    }

    @ProgressChange
    void progressChange(){

    }


    @RequestDataSucc
    void requestdataSucc(){

    }
}
