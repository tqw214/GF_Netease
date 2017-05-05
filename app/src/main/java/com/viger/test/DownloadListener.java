package com.viger.test;

/**
 * Created by Administrator on 2017/4/7.
 */

public interface DownloadListener {

    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCancled();



}
