package com.example.lijian.myapplication.utils;

/**
 * Created by LIJIAN on 2017/3/29.
 */

public class JniUtil {
   static {
       System.loadLibrary("JniUtil");
   }
    public static native String getStringFromC();
}
