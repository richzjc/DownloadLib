package com.richzjc.rdownload;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.richzjc.download.ConstKt;
import com.richzjc.download.notify.Observer;
import com.richzjc.download.task.ParentTask;
import com.richzjc.rdownload.widget.ProgressWscnImageView;
import java.util.ArrayList;
import java.util.List;

public class RvAdapter<T extends ParentTask> extends RecyclerView.Adapter {

    List<T> list = new ArrayList<>();

    public void setData(List<T> datas) {
        if (datas != null) {
            list.clear();
            list.addAll(datas);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyHolder) holder).doBindData(list.get(position));
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((MyHolder)holder).downloadTask.removeObserver(((MyHolder) holder).observer);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ProgressWscnImageView image;
        TextView showState;
        RelativeLayout imageParent;
        TextView newsTitle;
        TextView newsTime;

        Observer observer = new Observer() {
            @Override
            public void notifyRequestData() {
                if(downloadTask != null)
                    doBindData(downloadTask);
            }

            @Override
            public void notifyProgress() {
                if(downloadTask != null)
                    setStatus(downloadTask);
            }

            @Override
            public void notifyStatus() {
                if(downloadTask != null)
                    setStatus(downloadTask);
            }
        };

        T downloadTask;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            showState = itemView.findViewById(R.id.showState);
            imageParent = itemView.findViewById(R.id.image_parent);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsTime = itemView.findViewById(R.id.news_time);
        }

        public void doBindData(T downloadTask) {
            this.downloadTask = downloadTask;
            downloadTask.registObserver(observer);
            newsTitle.setText(((DownloadTask)downloadTask).title);
            setStatus(downloadTask);
        }


        private void setStatus(T downloadTask) {
            Log.i("download", "init");
            if (downloadTask != null) {
                if (downloadTask.getStatus() == ConstKt.DOWNLOADING) {
                    showState.setText(downloadTask.getProgress() + "%");
                } else if (downloadTask.getStatus() == ConstKt.DOWNLOAD_FINISH) {
                    showState.setText("下载完成");
                } else if (downloadTask.getStatus() == ConstKt.WAITING) {
                    showState.setText("等待缓存");
                } else if (downloadTask.getStatus() == ConstKt.DOWNLOAD_PAUSE) {
                    showState.setText("暂停下载");
                } else if (downloadTask.getStatus() == ConstKt.DOWNLOAD_ERROR) {
                    showState.setText("下载失败");
                }
            }
        }
    }
}
