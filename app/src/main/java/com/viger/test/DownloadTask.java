package com.viger.test;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/7.
 */

public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private DownloadListener mListener;
    private OkHttpClient mClient;

    public boolean isCanceled = false;
    public boolean isPaused = false;
    private int lastProgerss;
    private boolean isCanStart = true;

    public DownloadTask(DownloadListener listener) {
        this.mListener = listener;
        mClient = new OkHttpClient();
    }

    public void cancelDownload() {
        this.isCanceled = true;
    }

    public void pauseDownlod() {
        this.isPaused = true;
    }

    public boolean isCanStart() {
        return this.isCanStart;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        isCanStart = false;
    }

    @Override
    protected Integer doInBackground(String... params) {
        File file = null;
        InputStream is = null;
        RandomAccessFile saveFile = null;
        try {
            String downloadUrl = params[0];
            long downloadLength = 0;
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            Log.i("tag", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
            file = new File(directory, fileName);
            if(file.exists()) {
                downloadLength = file.length();
            }
            long contentLength = getContentLength(downloadUrl);
            if(contentLength == 0) {
                return TYPE_FAILED;
            }else if(contentLength == downloadLength) {
                return TYPE_SUCCESS;
            }
            Request request = new Request.Builder().addHeader("RANGE","bytes=" + downloadLength + "-").url(downloadUrl).build();
            Response response = mClient.newCall(request).execute();
            if(response != null) {
                is = response.body().byteStream();
                saveFile = new RandomAccessFile(file, "rw");
                saveFile.seek(downloadLength); //跳过已经下载的字节，从指定位置开始写入
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while((len = is.read(b)) != -1) {
                    if(isCanceled) {
                        return TYPE_CANCELED;
                    }else if(isPaused) {
                        return TYPE_PAUSED;
                    }else {
                        total += len;
                        saveFile.write(b, 0, len);
                        //计算下载的百分比
                        int progress = (int) (((total + downloadLength)) * 100 / contentLength);
                        publishProgress(progress);
                    }
                }
                response.close();
                return TYPE_SUCCESS;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null) is.close();
                if(saveFile != null) saveFile.close();
                //如果取消下载并且文件不为空,则删除
                if(isCanceled && file != null) {
                    file.delete();
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if(progress > lastProgerss) {
            mListener.onProgress(progress);
            lastProgerss = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer) {
            case TYPE_SUCCESS:
                if(mListener != null) mListener.onSuccess();
                break;
            case TYPE_FAILED:
                if(mListener != null) mListener.onFailed();
                break;
            case TYPE_CANCELED:
                if(mListener != null) mListener.onCancled();
                break;
            case TYPE_PAUSED:
                if(mListener != null) mListener.onPaused();
                break;
        }
        isCanStart = true;
    }

    private long getContentLength(String downloadUrl) throws IOException {
        Request request = new Request.Builder().url(downloadUrl).build();
        Response response = mClient.newCall(request).execute();
        if(response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }


}
