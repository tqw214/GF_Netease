package com.viger.test;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.viger.netease.R;
import com.viger.netease.Service.DownloadImgService;

/**
 * Created by Administrator on 2017/4/7.
 */

public class TestActivity extends Activity {

    private Button btn_start,btn_pause,btn_cancel;
    private Intent intent = null;
    private DownloadServcie.DownloadBinder downloadBinder;
    private String url = "http://apk.rtt1.net:8088/rtt.apk";
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn_start = (Button) findViewById(R.id.btn_begin);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        pb = (ProgressBar) findViewById(R.id.pb);
        intent = new Intent(this, DownloadServcie.class);
        //startService(intent);
        bindService(intent, conn, BIND_AUTO_CREATE);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadBinder.startDownload(url);
            }
        });
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadBinder.pauseDownload();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadBinder.cancelDownload();
            }
        });
    }

    private DownloadListener mListener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            pb.setProgress(progress);
        }

        @Override
        public void onSuccess() {
            Toast.makeText(TestActivity.this,"下载成功",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFailed() {
            Toast.makeText(TestActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            Toast.makeText(TestActivity.this,"下载暂停",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancled() {
            Toast.makeText(TestActivity.this,"下载取消",Toast.LENGTH_SHORT).show();
            if(pb != null) pb.setProgress(0);
        }
    };

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder = (DownloadServcie.DownloadBinder) iBinder;
            downloadBinder.setListener(mListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
