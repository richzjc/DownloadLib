package com.richzjc.rdownload;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.richzjc.download.okhttp.IRequestParamter;
import com.richzjc.download.task.ChildTask;
import com.richzjc.download.task.ParentTask;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DownloadTask extends ParentTask implements IRequestParamter {

    public int comment_count;
    public String content_short;
    public long display_time;
    public String id;
    public String image_uri;
    public String source_name;
    public String title;
    private String content;

    public DownloadTask(){

    }

    public DownloadTask(String id){
        this.id = id;
    }

    @Nullable
    @Override
    public String getRequestUrl() {
        return "https://api-wscn.xuangubao.cn/apiv1/content/articles/" + id + "?extract=1";
    }

    @Nullable
    @Override
    public Integer getRequestMethod() {
        return null;
    }

    @Nullable
    @Override
    public Map<String, String> getRequestHeader() {
        return null;
    }

    @Override
    public List<ChildTask> getChildTasks() {
        List<ChildTask> childTasks = new ArrayList<>();
        if(!TextUtils.isEmpty(image_uri)){
            childTasks.add(new ChildTask(image_uri));
        }
        return childTasks;
    }

    @Nullable
    @Override
    public String getResultJSONKey() {
        return "data";
    }

    @NonNull
    @Override
    public String toString() {
        return id + "; title = " + title;
    }
}
