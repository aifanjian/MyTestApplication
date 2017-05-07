package com.example.lijian.myapplication.dowload;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.example.lijian.myapplication.bean.DownloadInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import okhttp3.OkHttpClient;

/**
 * Created by LIJIAN on 2017/4/25.
 */

public class DownloadManager implements DownloadCallable.DownloadProgressListener {
    public static final String POSTFIX_FILE_DOWNLOADING = ".tmp";
    private static final String TAG = DownloadManager.class.getSimpleName();
    private String mSavePath;
    private ExecutorService mExecutor;
    private ArrayList<DownloadInfo> mDownloadList = new ArrayList<>();
    private ArrayMap<DownloadInfo, Future> mFutureMap = new ArrayMap<>();
    private Context mContext;
    private OkHttpClient mHttpClient;
    private File mDownFileDir;
    private Handler mHandler;
    private DownloadProgressListener mListener;


    public DownloadManager(Context context) {
        this(context, 0);
    }

    public DownloadManager(Context context, int threadPoolSize) {
        Log.e(TAG, "DownloadManager 构造");
        mContext = context;
        mSavePath = mContext.getExternalCacheDir().getAbsolutePath() + "/download";
        mDownFileDir = new File(mSavePath);
        mDownFileDir.mkdirs(); //初如化目录
        mExecutor = DownloadPoolExecutor.createPoolExecutor(threadPoolSize);
        mHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void setListener(DownloadProgressListener listener) {
        mListener = listener;
    }

    public ArrayList<DownloadInfo> getDownloadList() {
        return mDownloadList;
    }

    public void addDownloadInfo(String url) {
        DownloadInfo info = new DownloadInfo();
        info.setUrl(url);
        info.setName(url.substring(url.lastIndexOf("/") + 1));
        //给文件添加本地存储路径
        info.setSavePath(mSavePath);
        info.setState(DownloadInfo.STATE_WAITING);
        if (!mDownloadList.contains(info)) {
            mDownloadList.add(info);
        }
    }

    public void readyDownload(DownloadInfo info) {
        if (info == null || info.getUrl() == null) {
            showToast("下载地址为null");
            return;
        }
        //根据其状态进不同的操作
        switch (info.getState()) {
            case DownloadInfo.STATE_DOWNLOADING:
                showToast("当前任务正在下载中");
                break;
            case DownloadInfo.STATE_WAITING:
                //判断当前 任务是否已经下载过了，即在本地已存在。
                File file = new File(mSavePath + "/" + info.getName());
                if (file.exists()) {
                    showToast("当前文件已存在");
                    return;
                }
            case DownloadInfo.STATE_FAILED:
                startDownload(info, "开始下载任务");
                break;
            case DownloadInfo.STATE_PAUSE:
                startDownload(info, "继续下载任务");
                break;
        }
    }

    private void startDownload(DownloadInfo info, String message) {
        info.setState(DownloadInfo.STATE_DOWNLOADING);
        Future<?> future = mExecutor.submit(new FutureTask(new DownloadCallable(mHttpClient, info, this)));
        mFutureMap.put(info, future);
        showToast(message);
    }

    public void pauseDownload(DownloadInfo info) {
        Future future = mFutureMap.get(info);
        if (future == null) {
            showToast("当前任务还未开始");
            return;
        }
        mFutureMap.get(info).cancel(true);
    }

    //监听下载状态,注意以下回调是在子线程中调用的，因此不能用于更新UI
    @Override
    public void onProgress(final DownloadInfo info) {
        if (mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onProgress(info);
                }
            });
        }
    }

    @Override
    public void onPause(DownloadInfo info) {
        info.setState(DownloadInfo.STATE_PAUSE);
        mFutureMap.remove(info);
        showToast("任务已暂停");
    }

    @Override
    public void onFinish(final DownloadInfo info) {
        showToast(info.getName() + "下载完成");
        if (mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDownloadList.remove(info);
                    mFutureMap.remove(info);
                    mListener.onFinished();
                }
            });

        }
    }

    @Override
    public void onFailed(DownloadInfo info, Exception e) {
        info.setState(DownloadInfo.STATE_FAILED);
        Log.e(TAG, "发生异常，请重试： " + e.toString());
    }

    public void showToast(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 清除已经下载完成的文件
     */
    public void deleteDownloadFiles() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for (File file : mDownFileDir.listFiles()) {
                    if (!file.getName().endsWith(POSTFIX_FILE_DOWNLOADING)) {
                        file.delete();
                    }
                }
                showToast("文件清除完成");
            }
        });

    }

    public interface DownloadProgressListener {
        void onProgress(DownloadInfo info);

        void onFinished();
    }

}
