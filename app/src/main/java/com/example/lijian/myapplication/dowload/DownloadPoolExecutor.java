package com.example.lijian.myapplication.dowload;

import android.os.Process;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by LIJIAN on 2017/4/25.
 */

public class DownloadPoolExecutor {
    public static final int DEFAULT_THREAD_POOL_SIZE = 3;

    public static ExecutorService createPoolExecutor() {
        return createPoolExecutor(DEFAULT_THREAD_POOL_SIZE);
    }

    public static ExecutorService createPoolExecutor(int poolSize) {
        int size = poolSize > 0 ? poolSize : DEFAULT_THREAD_POOL_SIZE;
        return new ThreadPoolExecutor(
                size,
                size,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new DownloadThreadFactory());
    }

    /**
     * 自定义线程工厂类，为每个线程设置统一命名规则
     */
    static class DownloadThreadFactory implements ThreadFactory {
        private int count;
        private String name = DownloadThreadFactory.class.getSimpleName();

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread t = new DownloadThread(r, name + "_" + count++);
            t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    Log.e(name, e.getMessage(), e);
                }
            });
            return t;
        }
    }

    /**
     * 自定义线程类，为其设置线程优先级
     */
    static class DownloadThread extends Thread {
        public DownloadThread() {
        }

        public DownloadThread(Runnable runnable, String threadName) {
            super(runnable, threadName);
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            super.run();
        }
    }

}
