package com.richzjc.download.util;

import androidx.annotation.NonNull;

import com.richzjc.download.RDownloadClient;

import java.util.Collection;
import java.util.LinkedList;

public class RLinkdList extends LinkedList {

    RDownloadClient.Builder builder;

    @Override
    public boolean add(Object o) {
        return super.add(o);
    }

    @Override
    public boolean addAll(@NonNull Collection c) {
        return super.addAll(c);
    }
}
