package com.example.lijian.myapplication.bean;

import com.example.lijian.myapplication.utils.HashCodeHelper;

import java.net.URLEncoder;

/**
 * Created by LIJIAN on 2017/4/25.
 */

public class DownloadInfo {
    public static final int STATE_WAITING = 0;
    public static final int STATE_DOWNLOADING = 1;
    public static final int STATE_PAUSE = 2;
    public static final int STATE_FAILED = 3;
    private int state;
    private long totalLength;
    private long downloadedLength;
    private String url;
    private String savePath;
    private String name;

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }

    public void setDownloadedLength(long downloadedLength) {
        this.downloadedLength = downloadedLength;
    }

    public long getDownloadedLength() {
        return downloadedLength;
    }

    public void addDownloadedLength(long downloadedLength) {
        this.downloadedLength += downloadedLength;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DownloadInfo)) {
            return false;
        }
        return url.equals(((DownloadInfo) obj).getUrl());
    }

    @Override
    public int hashCode() {
        return HashCodeHelper.getInstance().appendObj(url).getHashCode();
    }
}
