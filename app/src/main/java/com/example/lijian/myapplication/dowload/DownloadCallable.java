package com.example.lijian.myapplication.dowload;


import android.text.TextUtils;

import com.example.lijian.myapplication.bean.DownloadInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LIJIAN on 2017/4/25.
 */

public class DownloadCallable implements Callable<Integer> {
    private static final String TAG = DownloadCallable.class.getSimpleName();
    private OkHttpClient mHttpClient;
    private DownloadInfo mDownloadInfo;
    private DownloadProgressListener mListener;
    private File mTempFile;


    public DownloadCallable(OkHttpClient client, DownloadInfo info, DownloadProgressListener listener) {
        mHttpClient = client;
        mDownloadInfo = info;
        mListener = listener;
    }

    @Override
    public Integer call() {
        //增加判断本地是否有未下载完成的临时文件
        mTempFile = new File(mDownloadInfo.getSavePath(), mDownloadInfo.getName() + DownloadManager.POSTFIX_FILE_DOWNLOADING);
        Request request;
        if (mTempFile.exists()) {
            mDownloadInfo.setDownloadedLength(mTempFile.length());
            request = new Request.Builder().url(mDownloadInfo.getUrl()).header("Range", "bytes=" + mTempFile.length() + "-").build();
        } else {
            request = new Request.Builder().url(mDownloadInfo.getUrl()).build();
        }
        Call call = mHttpClient.newCall(request);
        //执行下载任务，并写入本地文件中
        try {
            writeToFile(call);
        } catch (Exception e) {
            if (mListener != null) {
                mListener.onFailed(mDownloadInfo, e);
            }
        }
        //正常退出
        return 0;
    }

    private void writeToFile(Call call) throws IOException {
        Response response;
        InputStream inputStream;
        FileOutputStream outputStream;
        response = call.execute();
        if (!response.isSuccessful()) {
            throw new IOException("下载响应异常" + response);
        }
        String responseRange = response.headers().get("Content-Range");
        inputStream = response.body().byteStream();
        // 判断是否支持断点续传
        if (!TextUtils.isEmpty(responseRange) && responseRange.contains(String.valueOf(mTempFile.length()))) {
            outputStream = new FileOutputStream(mTempFile, true); //为true 则追加数据，false 则从头写入
        } else {
            outputStream = new FileOutputStream(mTempFile, false);
        }
        if (mTempFile.exists()) {
            mDownloadInfo.setTotalLength(response.body().contentLength() + mDownloadInfo.getDownloadedLength());
        } else {
            mDownloadInfo.setTotalLength(response.body().contentLength());
        }
        //将文件写入本地
        byte[] buffer = new byte[1024 * 8];
        int length;
        boolean isInterrupt = false;
        while ((length = inputStream.read(buffer)) != -1) {
            if (Thread.currentThread().isInterrupted()) {
                isInterrupt = true;
                break;
            }
            outputStream.write(buffer, 0, length);
            //更新DownloadInfo 的已下载长度值
            if (mListener != null) {
                mDownloadInfo.addDownloadedLength(length);
                mListener.onProgress(mDownloadInfo);
            }
        }
        if (outputStream != null) {
            outputStream.flush();
            outputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        //暂停下载，结束执行
        if (isInterrupt) {
            if (mListener != null) {
                mListener.onPause(mDownloadInfo);
            }
            return;
        }
        //下载完成
        mListener.onFinish(mDownloadInfo);
        mTempFile.renameTo(new File(mDownloadInfo.getSavePath(), mDownloadInfo.getName()));
    }

    interface DownloadProgressListener {
        void onProgress(DownloadInfo info);

        void onPause(DownloadInfo info);

        void onFinish(DownloadInfo info);

        void onFailed(DownloadInfo info, Exception e);
    }

}
