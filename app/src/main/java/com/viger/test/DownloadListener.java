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

    //这里模拟修改了代码 git——test


}
