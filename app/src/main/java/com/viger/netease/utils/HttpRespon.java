package com.viger.netease.utils;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/4/14.
 */

public abstract class HttpRespon<T> {

    private Class<T> t;

    public HttpRespon(Class<T> t) {
        this.t = t;
    }

    public abstract void onSuccess(T result);
    public abstract void onError(String msg);

    public void parseJson(String json) {
        if(TextUtils.isEmpty(json)) {
            onError("网络连接失败");
            return;
        }
        if(t == String.class) {
            onSuccess((T)json);
            return;
        }
        T result = JsonUtil.parseJson(json, t);
        if(result != null) {
            onSuccess(result);
        }else {
            onError("json解析失败");
        }
    }

}
