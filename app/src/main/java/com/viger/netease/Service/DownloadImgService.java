package com.viger.netease.Service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.viger.netease.bean.AdsBean;
import com.viger.netease.utils.CloseableUtil;
import com.viger.netease.utils.Constant;
import com.viger.netease.utils.Md5Helper;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2017/4/5.
 */

public class DownloadImgService extends IntentService {

    public DownloadImgService() {
        super("DownloadImgService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AdsBean ads = (AdsBean) intent.getSerializableExtra("ads");
        List<AdsBean.AdsDetail> ads1 = ads.getAds();
        if(ads1 != null && ads1.size()>0) {
            for (AdsBean.AdsDetail detail: ads1) {
                List<String> res_url = detail.getRes_url();
                if(res_url != null) {
                    String img_url = res_url.get(0); //获取每一张图片的url
                    if(!TextUtils.isEmpty(img_url)) {
                        //开始下载图片
                        Log.i("tag",img_url);
                        String img_cache_name = Md5Helper.toMD5(img_url);
                        //判断图片是否已经下载过
                        if(!checkImgIsDownlod(img_cache_name)) {
                            //图片没有下载过
                            downloadImage(img_cache_name, img_url);
                        }else {
                            Log.i("tag", "图片已存在,无需下载");
                        }
                    }
                }
            }
        }
    }

    private boolean checkImgIsDownlod(String imgNameMd5) {
        File cacheDir = getSaveImgPath();
        File temp_img = new File(cacheDir, imgNameMd5 + ".jpg");
        if(temp_img.exists()) {
            //存在
            Bitmap bitmap = BitmapFactory.decodeFile(temp_img.getAbsolutePath());
            if(bitmap != null) {
                Log.i("tag", "图片已经存在");
                return true;
            }
        }
        //不存在
        return false;
    }

    private void downloadImage(String cache_name, String imgUrl) {
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(imgUrl);
            conn = (HttpURLConnection) url.openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            if(bitmap != null) {
                //保存图片到sd卡
                saveImgToSD(cache_name, bitmap);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void saveImgToSD(String cacheName, Bitmap bitmap) {
        if(bitmap == null) return;
        FileOutputStream fos = null;
        try {
            File file = new File(getSaveImgPath(), cacheName + ".jpg");
            if(file.exists()) return;
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            CloseableUtil.close(fos);
        }

    }

    private File getSaveImgPath() {
        File path = null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File rootPath = Environment.getExternalStorageDirectory();
            path = new File(rootPath, Constant.CACHE);
            if(!path.exists()) {
                path.mkdirs();
            }
        }else {
            File cachePath = this.getCacheDir();
            path = new File(cachePath, Constant.CACHE);
            if(!path.exists()) {
                path.mkdirs();
            }
        }
        return path;
    }

}
