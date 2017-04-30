package com.example.lijian.myapplication.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lijian.myapplication.R;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ListView mListView;
    private String[] mItemNames = {"属性动画测试", "JNI调用测试", "圆形进度测试", "下载管理测试"};
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        init();
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.list);
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItemNames));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(mContext, PropertyAnimationTestActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(mContext, JniTestActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(mContext, CircleProgressActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(mContext, DownloadActivity.class));
                        break;
                }
            }
        });

    }
}
