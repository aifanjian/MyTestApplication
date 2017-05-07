package com.example.lijian.myapplication.view;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import static com.example.lijian.myapplication.utils.PermissionChecker.hasPermissions;

/**
 * Created by LIJIAN on 2017/5/6.
 */

class BaseActivity extends AppCompatActivity {
    public static final int CODE_REQUEST_PERMISSION = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void requestPermission_only(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, CODE_REQUEST_PERMISSION);
    }

    protected void requestPermission(String... permissions) {
        if (hasPermissions(permissions)) {
            executePermissionRequestSuccess();
        } else {
            ActivityCompat.requestPermissions(this, permissions, CODE_REQUEST_PERMISSION);
        }
    }


    protected boolean hasPermissions(String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    protected boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (CODE_REQUEST_PERMISSION == requestCode && requestPermissionsSuccess(grantResults)) {
            executePermissionRequestSuccess();
        } else {
            executePermissionRequestFailed();
        }
    }

    /**
     * 检查权限请求是否全部成功
     *
     * @param grantResults
     * @return
     */
    private boolean requestPermissionsSuccess(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 请求权限成功时调用的方法，需要子类重写
     */
    protected void executePermissionRequestSuccess() {

    }

    /**
     * 请求权限失败时调用的方法，需要子类重写
     */
    protected void executePermissionRequestFailed() {

    }
}
