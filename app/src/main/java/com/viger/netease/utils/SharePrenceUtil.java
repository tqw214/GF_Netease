package com.viger.netease.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/4/5.
 */

public class SharePrenceUtil {

    public static final String XML_FILE_NAME = "cache";

    public static void saveString(Context ctx, String title, String content) {
        SharedPreferences sp = ctx.getSharedPreferences(XML_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(title, content).apply();
    }

    public static String getString(Context ctx, String title) {
        SharedPreferences sp = ctx.getSharedPreferences(XML_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(title, "");
    }

    public static void saveInt(Context ctx, String title, int value) {
        SharedPreferences sp = ctx.getSharedPreferences(XML_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(title, value).apply();
    }

    public static int getInt(Context ctx, String title) {
        SharedPreferences sp = ctx.getSharedPreferences(XML_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(title, 0);
    }

    public static void saveLong(Context ctx, String title, long value) {
        SharedPreferences sp = ctx.getSharedPreferences(XML_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putLong(title, value).apply();
    }

    public static long getLong(Context ctx, String title) {
        SharedPreferences sp = ctx.getSharedPreferences(XML_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getLong(title, 0);
    }

}
