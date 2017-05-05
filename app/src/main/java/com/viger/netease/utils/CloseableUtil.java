package com.viger.netease.utils;

import java.io.Closeable;

/**
 * Created by Administrator on 2017/4/5.
 */

public class CloseableUtil {

    public static void close(Closeable obj) {
        try {
            if(obj != null) obj.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
