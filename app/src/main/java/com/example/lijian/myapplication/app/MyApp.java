package com.example.lijian.myapplication.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by LIJIAN on 2017/4/9.
 */

public class MyApp extends Application {
    public static Context appContext ;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }
}
