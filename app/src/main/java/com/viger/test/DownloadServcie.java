package com.viger.test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/4/7.
 */

public class DownloadServcie extends Service {

    private DownloadTask downloadTask;
    private DownloadBinder mBinder = new DownloadBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class DownloadBinder extends Binder {

        private DownloadListener mListener;

        public void setListener(DownloadListener listener) {
            this.mListener = listener;
        }

        public void startDownload(String url) {
            //如果正在下载中，不允许再次启动
            downloadTask = new DownloadTask(mListener);
            if(downloadTask.isCanStart()) {
                downloadTask.execute(url);
            }
        }

        public void pauseDownload() {
            if(downloadTask != null) {
                downloadTask.pauseDownlod();
            }
        }

        public void cancelDownload() {
            if(downloadTask != null) {
                downloadTask.cancelDownload();
            }
        }

    }

}
