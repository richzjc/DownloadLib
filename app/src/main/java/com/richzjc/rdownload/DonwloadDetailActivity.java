package com.richzjc.rdownload;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.richzjc.download.RDownloadClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DonwloadDetailActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    RDownloadClient downloadClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paid_recycler_item_downloading_article);
        ButterKnife.bind(this, this);
        downloadClient = RDownloadClient.Companion.bind(this);
        init();
    }

    private void init(){
        int initId = 3349971;
        for(int i = 0; i < 20; i++){
            downloadClient.addTask(new DownloadTask(String.valueOf(i + initId)));
        }

        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        RvAdapter adapter = new RvAdapter();
        adapter.setData(downloadClient.getAllDownloadData());
        rv.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RDownloadClient.Companion.unBind(this);
    }
}
