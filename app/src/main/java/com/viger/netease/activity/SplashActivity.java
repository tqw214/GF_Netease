package com.viger.netease.activity;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.viger.netease.R;
import com.viger.netease.Service.DownloadImgService;
import com.viger.netease.bean.AdsBean;
import com.viger.netease.ui.TimeView;
import com.viger.netease.utils.Constant;
import com.viger.netease.utils.JsonUtil;
import com.viger.netease.utils.Md5Helper;
import com.viger.netease.utils.SharePrenceUtil;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/5.
 */

public class SplashActivity extends Activity {

    private ImageView mAds;
    private OkHttpClient client;
    private static final String JSON_CACHE = "ads_json";
    private static final String JSON_CACHE_TIMEOUT = "ads_json_timeout";
    private static final String JSON_LAST_TIME = "ads_json_last_time";
    private static final String ADS_INDEX = "ads_index";
    private TimeView timeView;
    private int time = 1 * 1000; //倒计时时长

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //4.4沉浸式
        //View decorView = getWindow().getDecorView();
        //decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);

        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        mAds = (ImageView) findViewById(R.id.iv_ads);
        timeView = (TimeView) findViewById(R.id.timeView);
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gotoMainActivity();
                //timeView.stop();
            }
        });
        //timeView.setProgress(time);
        timeView.setOnFinishListener(new TimeView.onFinishListener() {
            @Override
            public void onfinish() {
                gotoMainActivity();
            }

            @Override
            public void onInto() {
                gotoMainActivity();
            }
        });
        getAds();
        showImage();
    }

    private void gotoMainActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    private void showImage() {
        String cache = SharePrenceUtil.getString(this,JSON_CACHE);
        if(!TextUtils.isEmpty(cache)) { //如果缓存不为空
            int index = SharePrenceUtil.getInt(this, ADS_INDEX);
            AdsBean ads = JsonUtil.parseJson(cache, AdsBean.class);
            if(null == ads) {
                return;
            }
            List<AdsBean.AdsDetail> ads1 = ads.getAds();
            if(null != ads1 && ads1.size() > 0) {
                final AdsBean.AdsDetail adsDetail = ads1.get(index%ads1.size());
                index++;
                SharePrenceUtil.saveInt(this, ADS_INDEX, index);
                List<String> res_url = adsDetail.getRes_url();

                if(null!=res_url && !TextUtils.isEmpty(res_url.get(0))) {
                    String imgName = Md5Helper.toMD5(res_url.get(0)) + ".jpg";
                    String path = Environment.getExternalStorageDirectory().getPath() + File.separator + Constant.CACHE + File.separator + imgName;
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    if(bitmap != null && mAds != null) {
                        mAds.setImageBitmap(bitmap);
                        mAds.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                timeView.stop(); //点击进入广告页面停止倒计时
                                String link_url = adsDetail.getAction_params().getLink_url();
                                if(!TextUtils.isEmpty(link_url)) {
                                    //跳到广告网页
                                    Intent intent = new Intent(SplashActivity.this, WebViewActivity.class);
                                    intent.putExtra(WebViewActivity.ACTION_URL, link_url);
                                    startActivity(intent);
                                    //finish();
                                }
                            }
                        });
                        //图片显示出来了开始倒计时
                        timeView.setProgress(time);
                    }
                }
            }
        }else {
            //如果缓存为空,则3秒后直接跳转
            mHandler.sendEmptyMessageDelayed(0, 3000);
        }

    }

    private void getAds() {
        //先看看有没有缓存
        String cache = SharePrenceUtil.getString(this, JSON_CACHE);
        int timeout = SharePrenceUtil.getInt(this, JSON_CACHE_TIMEOUT);
        long time = SharePrenceUtil.getLong(this, JSON_LAST_TIME);
        long duration = System.currentTimeMillis() - time;
        if(!TextUtils.isEmpty(cache) && (timeout * 1000 * 60 > duration)) {
            //有缓存并且没有超时直接下载图片
            Log.i("tag","有缓存并且没有超时则直接下载图片");
            startServiceDownloadImg(cache);
            return;
        }
        Log.i("tag","没有缓存则请求网络获取图片下载地址");
        client = new OkHttpClient();
        Request request = new Request.Builder().url(Constant.SPLASH_URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()) {
                    //失败
                }
                String json = response.body().string();
                startServiceDownloadImg(json);
            }
        });
    }

    //开启服务下载图片
    private void startServiceDownloadImg(String json) {
        AdsBean ads = JsonUtil.parseJson(json, AdsBean.class);
        if(null != ads) {
            //缓存json数据
            SharePrenceUtil.saveString(SplashActivity.this, JSON_CACHE, json);
            SharePrenceUtil.saveInt(SplashActivity.this, JSON_CACHE_TIMEOUT, ads.getNext_req());
            SharePrenceUtil.saveLong(SplashActivity.this, JSON_LAST_TIME, System.currentTimeMillis());

            //请求成功
            //Log.i("tag", result);
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, DownloadImgService.class);
            intent.putExtra("ads", ads);
            startService(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeView.stop();
    }
}
