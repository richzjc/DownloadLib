package com.richzjc.rdownload;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

            }

            @Override
            public void notifyProgress() {

            }

            @Override
            public void notifyStatus() {

            }
        };


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            showState = itemView.findViewById(R.id.showState);
            imageParent = itemView.findViewById(R.id.image_parent);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsTime = itemView.findViewById(R.id.news_time);
        }

        public void doBindData(T downloadTask) {
            newsTitle.setText(((DownloadTask)downloadTask).title);
        }

    }
}
