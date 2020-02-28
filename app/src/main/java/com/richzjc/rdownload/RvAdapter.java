package com.richzjc.rdownload;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.richzjc.download.task.ParentTask;
import com.richzjc.rdownload.widget.ProgressWscnImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ProgressWscnImageView image;
        @BindView(R.id.showState)
        TextView showState;
        @BindView(R.id.image_parent)
        RelativeLayout imageParent;
        @BindView(R.id.news_title)
        TextView newsTitle;
        @BindView(R.id.news_time)
        TextView newsTime;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void doBindData(T downloadTask) {
            newsTitle.setText(((DownloadTask)downloadTask).title);
        }
    }
}
