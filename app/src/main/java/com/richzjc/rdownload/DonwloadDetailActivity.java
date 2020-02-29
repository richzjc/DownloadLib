package com.richzjc.rdownload;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.richzjc.download.RDownloadClient;
import com.richzjc.downloadannotation.SizeChange;


public class DonwloadDetailActivity extends AppCompatActivity {

    RecyclerView rv;
    TextView downloadCount;

    RDownloadClient downloadClient;

    @SizeChange
    public void onSizeChange(int size){
        downloadCount.setText("下载队列里面的数量有： " +  size);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paid_recycler_item_downloading_article);
        rv = findViewById(R.id.rv);
        downloadCount = findViewById(R.id.downloadCount);
        downloadClient = RDownloadClient.Companion.bind(this);
        init();
    }

    private void init(){
        int initId = 3586206;
        for(int i = 0; i < 10; i++){
            downloadClient.addTask(new DownloadTask(String.valueOf(initId - i)));
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
