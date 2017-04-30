package com.example.lijian.myapplication.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.example.lijian.myapplication.R;
import com.example.lijian.myapplication.custom.CircleProgress;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LIJIAN on 2017/4/22.
 */

public class CircleProgressActivity extends AppCompatActivity {
    @BindView(R.id.mCircleProgress)
    CircleProgress mCircleProgress;
    @BindView(R.id.mSeekBar)
    SeekBar mSeekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TEST","onCreate()..........................");
        setContentView(R.layout.activity_progress_circle);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Log.e("TEST","init()..........................");
        mCircleProgress.setPercent(0);
        mSeekBar.setMax(100);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCircleProgress.setPercent(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
