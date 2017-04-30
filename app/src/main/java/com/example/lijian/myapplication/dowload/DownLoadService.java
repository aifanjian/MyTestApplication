package com.example.lijian.myapplication.dowload;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by LIJIAN on 2017/4/25.
 */

public class DownLoadService extends Service {
    private LocalBinder mBinder = new LocalBinder();
    private DownloadManager mDownLoadManager;
    private static final String TAG = DownLoadService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate................");
        mDownLoadManager = new DownloadManager(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(startId, new Notification());
        Log.e(TAG, "onStartCommand................");
        return super.onStartCommand(intent, START_FLAG_REDELIVERY, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind................");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy................");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind................");
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public DownloadManager getManager() {
            return mDownLoadManager;
        }
    }

}
