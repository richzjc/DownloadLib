package com.richzjc.rdownload;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.richzjc.downloadannotation.PauseAll;
import com.richzjc.downloadannotation.StartAll;
import com.richzjc.downloadannotation.SizeChange;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SizeChange
    void onSizeChange(){

    }

    @PauseAll
    void progressChange(){

    }


    @StartAll
    void requestdataSucc(){

    }
}
