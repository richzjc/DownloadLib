package com.richzjc.download.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.richzjc.download.RDownloadClient;
import com.richzjc.download.task.ChildTask;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadUtil {

    public static String getDownloadFilePath(RDownloadClient.Builder builder, ChildTask task) {
        String filePath = "";
        String fileName = "";
        if (!TextUtils.isEmpty(builder.getFilePath())) {
            filePath = builder.getFilePath();
        } else {
            File file = builder.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            if (file == null)
                file = getExternalCacheDir(builder.getContext());
            filePath = file.getAbsolutePath();
        }

        if(!TextUtils.isEmpty(task.fileName)){
            fileName = task.fileName;
        }else{
           fileName =  String.valueOf(task.requestUrl.hashCode());
        }
        return TextUtils.concat(filePath, "/", fileName, getSuffix(task.requestUrl)).toString();
    }

    private static String getSuffix(String downloadUrl) {
        String url = downloadUrl;
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String suffixes = "[\\w]+[\\.](avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|txt|html|zip|java|doc)";
        Pattern pat = Pattern.compile(suffixes);//正则判断
        Matcher mc = pat.matcher(url);//条件匹配
        String suffix = "";
        while (mc.find()) {
            suffix = mc.group();//截取文件名后缀名
        }
        if (!TextUtils.isEmpty(suffix)) {
            return suffix.substring(suffix.lastIndexOf("."));
        } else {
            return ".temp";
        }
    }

    public static File getExternalCacheDir(Context context) {
        return new File(Environment.getExternalStorageDirectory().getPath() + ("/Android/data/" + context.getPackageName() + "/download"));
    }
}
