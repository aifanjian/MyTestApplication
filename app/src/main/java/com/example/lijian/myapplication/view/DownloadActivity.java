package com.example.lijian.myapplication.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijian.myapplication.R;
import com.example.lijian.myapplication.bean.DownloadInfo;
import com.example.lijian.myapplication.custom.WithNumberProgressBar;
import com.example.lijian.myapplication.dowload.DownLoadService;
import com.example.lijian.myapplication.dowload.DownloadManager;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by LIJIAN on 2017/4/25.
 */

public class DownloadActivity extends AppCompatActivity implements DownloadManager.DownloadProgressListener {
    private static final String TAG = DownloadActivity.class.getSimpleName();
    @BindView(R.id.mListView)
    RecyclerView mListView;
    @BindView(R.id.mBt_download)
    Button mBtDownload;
    @BindView(R.id.mBt_delete)
    Button mBtDelete;
    private DownloadManager mManger;
    private Context mContext;
    private ArrayList<DownloadInfo> mInfoList;
    private String[] mUrls = new String[]{
            "http://10.100.0.1/IXCb4cdf31944e4c73d1ed7cb44499d8f01/data/wisegame/41e4d8d8127bb502/baidushoujizhushou_16793302.apk"
            , "http://10.100.0.1/IXC5c5ea5b07328a69f1bef3bd21977b290/data/wisegame/02ba8a69a5a792b1/QQ_500.apk"
            , "http://gdown.baidu.com/data/wisegame/2d5bf81de4e0ca42/weixin_1041.apk"
            , "http://10.100.0.1/IXC701ba3444b57cab23c3e64f581341acf/data/wisegame/4f3914ff7cbe7d30/shoujijingdong_45473.apk"
    };
    private DownloadAdapter mAdapter;
    private ServiceConnection mConnection;
    DecimalFormat mDecimalFormat = new DecimalFormat("0.00");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mContext = this;
        mConnection = new DownloadServiceConnection();
        mInfoList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(layoutManager);
        mAdapter = new DownloadAdapter();
        mListView.setAdapter(mAdapter);
        initService();
    }

    /**
     * 初始化下载服务
     */
    private void initService() {
        Intent serviceIntent = new Intent(this, DownLoadService.class);
        startService(serviceIntent);
        bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
    }


    private int index;

    @OnClick({R.id.mBt_download, R.id.mBt_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mBt_download:
                if (mManger != null) {
                    if (index < mUrls.length) {
                        mManger.addDownloadInfo(mUrls[index++]);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(mContext, "没有更多下载资源了", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.mBt_delete:
                mManger.deleteDownloadFiles();
                break;
        }
    }

    @Override
    public void onProgress(DownloadInfo info) {
        int position = mInfoList.indexOf(info);
        if (position >= 0) {
            View view = mListView.getChildAt(position);
            DownloadViewHolder viewHolder = (DownloadViewHolder) mListView.getChildViewHolder(view);
            float downloadSize = info.getDownloadedLength();
            float totalSize = info.getTotalLength() ;
            viewHolder.pb_progress.setProgress((int) (downloadSize * 100 / totalSize));
            viewHolder.tv_size.setText(mDecimalFormat.format(downloadSize / 1024 / 1024) + "M /" + mDecimalFormat.format(totalSize / 1024 / 1024) + "M");
            Log.e(TAG, "name: " + viewHolder.tv_name.getText() + "position: " + position);
        }
    }

    @Override
    public void onFinished() {
        mAdapter.notifyDataSetChanged();
    }

    class DownloadServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mManger = ((DownLoadService.LocalBinder) service).getManager();
            if (mManger != null) {
                mInfoList = mManger.getDownloadList();
                mManger.setListener(DownloadActivity.this);
                if (mInfoList.size() > 0) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mManger = null;
        }
    }

    class DownloadAdapter extends RecyclerView.Adapter<DownloadViewHolder> {
        @Override
        public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.e(TAG, "onCreateViewHolder...............");
            return new DownloadViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list_download, parent, false));
        }

        @Override
        public void onBindViewHolder(DownloadViewHolder holder, int position) {
            Log.e(TAG, "onBindViewHolder...............  " + position);
            DownloadInfo info = mInfoList.get(position);
            holder.tv_name.setText(info.getName());
            holder.pb_progress.setProgress(0);
            float downloadSize = info.getDownloadedLength();
            float totalSize = info.getTotalLength();
            int percent = 0;
            if (downloadSize > 0 && totalSize > 0) {
                percent = (int) (downloadSize * 100 / totalSize);
            }
            holder.pb_progress.setProgress(percent);
            holder.tv_size.setText(mDecimalFormat.format(downloadSize / 1024 / 1024) + "M /" + mDecimalFormat.format(totalSize / 1024 / 1024) + "M");
        }

        @Override
        public int getItemCount() {
            return mInfoList.size();
        }
    }

    class DownloadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_name, tv_size;
        private WithNumberProgressBar pb_progress;
        private Button bt_start, bt_pause;

        public DownloadViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
            pb_progress = (WithNumberProgressBar) itemView.findViewById(R.id.pb_progress);
            bt_start = (Button) itemView.findViewById(R.id.bt_start);
            bt_pause = (Button) itemView.findViewById(R.id.bt_pause);
            bt_pause.setOnClickListener(this);
            bt_start.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DownloadInfo info = mInfoList.get(getLayoutPosition());
            switch (v.getId()) {
                case R.id.bt_start:
                    mManger.readyDownload(info);
                    break;
                case R.id.bt_pause:
                    mManger.pauseDownload(info);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
