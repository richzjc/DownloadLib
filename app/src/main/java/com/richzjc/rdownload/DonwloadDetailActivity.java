package com.richzjc.rdownload;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Button pauseAll;
    Button startAll;

    RDownloadClient downloadClient;
//    String[] urls = new String[]{"http://img.tukuppt.com/newpreview_music/08/99/00/5c88d4a8d1f5745026.mp3",
//            "http://img.tukuppt.com/newpreview_music/08/99/00/5c88d4a8d1f5745026.mp3",
//            "http://img.tukuppt.com/newpreview_music/09/00/75/5c894afe4de5f1618.mp3",
//            "http://img.tukuppt.com/newpreview_music/09/00/32/5c89189c4f4cf81405.mp3",
//            "http://img.tukuppt.com/newpreview_music/08/98/97/5c88d1231eeb570304.mp3"};

    String[] urls = new String[]{"https://imtt.dd.qq.com/16891/apk/2D67F8F86F94891D637AFC14E2876F70.apk?fsname=com.tencent.tmgp.speedmobile_1.17.0.41206_1170041206.apk",
            "https://imtt.dd.qq.com/16891/apk/C48A73CCED637EF18A57BAD865A1714F.apk",
            "https://imtt.dd.qq.com/16891/apk/5AA1A2800257848F3195B2D0F41D5F3E.apk",
            "http://img.tukuppt.com/newpreview_music/09/00/32/5c89189c4f4cf81405.mp3",
            "http://img.tukuppt.com/newpreview_music/08/98/97/5c88d1231eeb570304.mp3"};

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
        pauseAll = findViewById(R.id.pauseAll);
        startAll = findViewById(R.id.startAll);
        downloadClient = RDownloadClient.Companion.bind(this);
        init();
        setListener();
    }

    private void setListener(){
        pauseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadClient.pauseAll();
            }
        });

        startAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadClient.startAll();
            }
        });
    }

    private void init(){
        int initId = 3586203;
        for(int i = 0; i < 5; i++){
            downloadClient.addTask(new DownloadTask(String.valueOf(initId - i), urls[i]));
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
