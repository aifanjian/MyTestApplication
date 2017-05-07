package com.example.lijian.myapplication.view;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.Toast;

import com.example.lijian.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LIJIAN on 2017/5/6.
 */

public class PermissionActivity extends BaseActivity {
    @BindView(R.id.mBt_permission)
    Button mBtPermission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.mBt_permission)
    public void onViewClicked() {
        requestPermission(Manifest.permission.READ_CONTACTS);
    }

    @Override
    protected void executePermissionRequestSuccess() {
        showToast("授权成功");
    }

    @Override
    protected void executePermissionRequestFailed() {
        showToast("授权失败");
    }

    void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }
}
