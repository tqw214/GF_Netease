package com.viger.netease.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/4/5.
 */

public class JsonUtil {

    private static Gson gson = null;

    public static <T> T parseJson(String json, Class<T> t) {
        if(gson == null) gson = new Gson();
        if(TextUtils.isEmpty(json) || t == null) return null;
        T back = gson.fromJson(json, t);
        return back;
    }

}
