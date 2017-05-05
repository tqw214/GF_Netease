package com.viger.netease.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/4/6.
 */

public class ImageUtil {

    public static File getImageFilePath(String imageNameMd5) {
        String sdCardRoot = Environment.getExternalStorageDirectory().getPath();
        String imgRoot = sdCardRoot + File.separator + Constant.CACHE + File.separator + imageNameMd5 + ".jpg";
        return new File(imgRoot);
    }

}
