package com.example.lijian.myapplication.utils;

import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.example.lijian.myapplication.app.MyApp;

/**
 * Created by LIJIAN on 2017/5/6.
 */

public class PermissionChecker {
    /**
     * 检查是否拥有某几种权限
     * @param permissions
     * @return true:拥有全部权限，否则返回false
     */
    public static boolean hasPermissions(String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查是否拥有某一种权限
     * @param permission
     * @return
     */
    public static boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(MyApp.appContext, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
