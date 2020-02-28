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
        downloadClient.addTask(new DownloadTask("3349971"));

        init();
    }

    private void init(){
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        RvAdapter adapter = new RvAdapter();
        rv.setAdapter(adapter);
    }

//    private void init() {
//        Log.i("download", "init");
//        if (parentTaskCallback != null) {
//            if (parentTaskCallback.status == DownloadConstants.DOWNLOADING) {
//                showState.setText(parentTaskCallback.progress + "%");
//            } else if (parentTaskCallback.status == DownloadConstants.DOWNLOAD_FINISH) {
//                showState.setText("下载完成");
//            } else if (parentTaskCallback.status == DownloadConstants.WAITING) {
//                showState.setText("等待缓存");
//            } else if (parentTaskCallback.status == DownloadConstants.DOWNLOAD_PAUSE) {
//                showState.setText("暂停下载");
//            } else if (parentTaskCallback.status == DownloadConstants.DOWNLOAD_ERROR) {
//                showState.setText("下载失败");
//            }
//        }
//    }

//    @ProgressSubscribe(configurationKey = "test")
//    private void updateProgress(ParentTaskCallback taskCallback) {
//        if (parentTaskCallback != null && TextUtils.equals(taskCallback.getParentTaskId(), parentTaskCallback.getParentTaskId())) {
//            init();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RDownloadClient.Companion.unBind(this);
    }
}
