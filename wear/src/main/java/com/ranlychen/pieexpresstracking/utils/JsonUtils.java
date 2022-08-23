package com.ranlychen.pieexpresstracking.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;


public class JsonUtils {
    private static final String TAG = "JsonUtils";
    public static <T> T parseJson(String json, Class<T> typeOfT) {
        try {
            return Reflect.on(typeOfT, sGson.fromJson(json, typeOfT)).fillNull().get();
        } catch (Exception e) {
            Log.e(TAG, "parse json fail: " + typeOfT.getName(), e);
            return null;
        }
    }

    public static <T> T parseJson(String json, Type typeOfT) {
        try {
            T data = sGson.fromJson(json, typeOfT);
            return Reflect.on(typeOfT, data).fillNull().get();
        } catch (Exception e) {
            Log.e(TAG, "parse json fail: type", e);
            return null;
        }
    }

    public static String toJson(Object src) {
        return sGson.toJson(src);
    }

    private static Gson sGson = new Gson();

    public static <T> T parseJsonWithThrowable(String json, Class<? extends T> typeOfT) throws Throwable {
        return Reflect.on(typeOfT, sGson.fromJson(json, typeOfT)).fillNull().get();
    }

}
