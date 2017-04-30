package com.example.lijian.myapplication.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.example.lijian.myapplication.R;
import com.example.lijian.myapplication.utils.JniUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LIJIAN on 2017/3/29.
 */

public class JniTestActivity extends AppCompatActivity {
    @BindView(R.id.bt_testJNI)
    Button btTestJNI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_jni);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_testJNI)
    public void onViewClicked() {
        Toast.makeText(this, JniUtil.getStringFromC(), Toast.LENGTH_LONG).show();
    }
}
