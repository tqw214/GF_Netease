package com.viger.netease.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/13.
 */

public class HttpUtil {

    private static HttpUtil INSTANCE = null;
    private static OkHttpClient mClick = null;

    private HttpUtil(){
        mClick = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();

    }

    public static HttpUtil getInstance() {
        if(INSTANCE == null) {
            synchronized (HttpUtil.class) {
                if(INSTANCE == null) {
                    INSTANCE = new HttpUtil();
                }
            }
        }
        return INSTANCE;
    }

    public void getDate(String url, final HttpRespon listener) {
        Request request = new Request.Builder().url(url).build();
        mClick.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
                listener.onError("连接服务器失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()) {
                    //失败
                    return;
                }
                String result = response.body().string();
                listener.parseJson(result);
            }
        });
    }

}
