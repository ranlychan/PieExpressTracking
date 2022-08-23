package com.ranlychen.pieexpresstracking.utils.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ranlychen.pieexpresstracking.utils.AppInfoUtil;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Config {

    private static final String TAG = "Config";
    private static Map<String, Config> mConfigMaps = new HashMap<>();
    private static String DEFAULT_NAME = null;


    private static HashMap<String, Object> mSharePreferenceCache = new HashMap<>();

    private String mName = "";
    private SharedPreferences mPreferences;

    //超时上报时间
    private static final int TIME_WAIT_FOR_REPORT_ANR = 2000;
    private static final String HANDLER_REPORT_ANR_NAME_KEY = "key";
    private static final int HANDLER_MESSAGE_KEY = 0X5666;

    private static volatile IConfig sConfigImpl;

    public static void init(Context c, IConfig configImpl) {
        if (null == sConfigImpl) {
            sConfigImpl = configImpl;
        }
    }

    public static void setDefaultName(String defaultName) {
        DEFAULT_NAME = defaultName;
    }

    public static synchronized Config getInstance(Context context) {
        if (null == DEFAULT_NAME) {
            DEFAULT_NAME = AppInfoUtil.getProcessName(context) + ".configuration";
        }

        return getConfig(context, DEFAULT_NAME, true);
    }

    public static synchronized Config getInstance(Context context, String name) {
        if (name == null) {
            name = "";
        }
        return getConfig(context, name, true);
    }

    public static synchronized Config getInstance(Context context, String name, boolean multiProc) {
        if (name == null) {
            name = "";
        }
        return getConfig(context, name, multiProc);
    }

    private static Config getConfig(Context context, String name, boolean multiProc) {
        Config config = null;
        if (mConfigMaps.containsKey(name)) {
            config = mConfigMaps.get(name);
        }
        if (config == null) {
            config = new Config(context, name, multiProc);
            mConfigMaps.put(name, config);
        }

        return config;
    }


    private Config(Context context, String name, boolean multiProc) {
        mName = name;
        if (null == sConfigImpl) {//自带默认sp实现,避免业务代码未初始化造成崩溃
            sConfigImpl = new DefaultConfigImpl();
        }
        mPreferences = sConfigImpl.getSpImpl(context, name, multiProc);
    }


    public void setOnChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listenner) {
        if (listenner != null) {
            mPreferences.registerOnSharedPreferenceChangeListener(listenner);
        }
    }

    public synchronized void remove(String key) {
        mPreferences.edit().remove(key).apply();
    }

    public synchronized boolean contains(String key){
        return mPreferences.contains(key);
    }

    public synchronized boolean setString(String key, String value) {
        SharedPreferences.Editor editor;
        if (null == value) {//适配mmkv,因为它不支持写入null
            editor = mPreferences.edit().remove(key);
        } else {
            editor = mPreferences.edit().putString(key, value);
        }
        return doApply(key, value, editor);
    }

    public synchronized boolean setStringSync(String key, String value) {
        SharedPreferences.Editor editor = mPreferences.edit().putString(key, value);
        return doCommit(editor);
    }

    public synchronized boolean setInt(String key, int value) {
        SharedPreferences.Editor editor = mPreferences.edit().putInt(key, value);
        return doApply(key, value, editor);
    }

    public synchronized boolean setIntSync(String key, int value) {
        SharedPreferences.Editor editor = mPreferences.edit().putInt(key, value);
        return doCommit(editor);
    }

    public synchronized boolean setLong(String key, long value) {
        SharedPreferences.Editor editor = mPreferences.edit().putLong(key, value);
        return doApply(key, value, editor);
    }

    public synchronized boolean setLongSync(String key, long value) {
        SharedPreferences.Editor editor = mPreferences.edit().putLong(key, value);
        return doCommit(editor);
    }

    public synchronized boolean setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mPreferences.edit().putBoolean(key, value);
        return doApply(key, value, editor);
    }

    public synchronized boolean setBooleanSync(String key, boolean value) {
        SharedPreferences.Editor editor = mPreferences.edit().putBoolean(key, value);
        return doCommit(editor);
    }

    public synchronized boolean setFloat(final String key, float value) {
        SharedPreferences.Editor editor = mPreferences.edit().putFloat(key, value);
        return doApply(key, value, editor);
    }

    public synchronized boolean setFloatSync(final String key, float value) {
        SharedPreferences.Editor editor = mPreferences.edit().putFloat(key, value);
        return doCommit(editor);
    }

    public synchronized boolean setStringSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = mPreferences.edit().putStringSet(key, value);
        return doApply(key, value, editor);
    }

    public synchronized boolean setStringSetSync(String key, Set<String> value) {
        SharedPreferences.Editor editor = mPreferences.edit().putStringSet(key, value);
        return doCommit(editor);
    }

    public synchronized String getString(String key, String defaultValue) {
        String cacheValue = getFromCache(mName, key, String.class);
        if (cacheValue != null) {
            return cacheValue;
        }
        return mPreferences.getString(key, defaultValue);
    }

    public synchronized int getInt(String key, int defaultValue) {
        Integer cacheValue = getFromCache(mName, key, Integer.class);
        if (cacheValue != null) {
            return cacheValue;
        }
        return mPreferences.getInt(key, defaultValue);
    }

    public synchronized long getLong(String key, long defaultValue) {
        Long cacheValue = getFromCache(mName, key, Long.class);
        if (cacheValue != null) {
            return cacheValue;
        }
        return mPreferences.getLong(key, defaultValue);
    }

    public synchronized boolean getBoolean(String key, boolean defaultValue) {
        Boolean cacheValue = getFromCache(mName, key, Boolean.class);
        if (cacheValue != null) {
            return cacheValue;
        }
        return mPreferences.getBoolean(key, defaultValue);
    }

    public synchronized float getFloat(final String key, final float defaultValue) {
        Float cacheValue = getFromCache(mName, key, Float.class);
        if (cacheValue != null) {
            return cacheValue;
        }
        return mPreferences.getFloat(key, defaultValue);
    }

    public synchronized Set<String> getStringSet(String key, Set<String> deaultValue) {
        Set cacheValue = getFromCache(mName, key, Set.class);
        try {
            if (cacheValue != null && cacheValue.size() > 0
                    && cacheValue.getClass().getGenericSuperclass() != null
                    && ((ParameterizedType) cacheValue.getClass().getGenericSuperclass()).getActualTypeArguments() != null
                    && ((ParameterizedType) cacheValue.getClass().getGenericSuperclass()).getActualTypeArguments().length > 0
                    && ((ParameterizedType) cacheValue.getClass().getGenericSuperclass()).getActualTypeArguments()[0].toString().getClass().toString()
                    .equals("class java.lang.String")) {
                return cacheValue;
            }
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
        }
        return mPreferences.getStringSet(key, deaultValue);
    }

    public synchronized boolean clearAllSync() {
        SharedPreferences.Editor editor = mPreferences.edit().clear();
        return doCommit(editor);
    }

    public synchronized void clearAllAsync() {
        mPreferences.edit().clear().apply();
    }

    public synchronized boolean clearAsync(final String key) {
        if(mPreferences.contains(key)){
            SharedPreferences.Editor editor = mPreferences.edit().remove(key);
            return doCommit(editor);
        }
        return true;
    }

    private boolean doCommit(SharedPreferences.Editor editor) {
        boolean result;
        try {
            result = editor.commit();
        } catch (Throwable t) {
            Log.e(TAG, t.toString());
            result = false;
        }
        return result;
    }

    private boolean doApply(final String key, Object value, final SharedPreferences.Editor editor) {
        try {
            editor.apply();
        } catch (Throwable t) {
            Log.e(TAG, t.toString());
            return false;
        }
        return true;
    }

    private static <T> T getFromCache(String name, String key, Class<T> clz) {
        String cacheKey = mergeNameAndKey(name, key);
        Object value = mSharePreferenceCache.get(cacheKey);
        if (value != null && clz.isInstance(value)) {
            return (T) value;
        } else {
            return null;
        }
    }

    private static String mergeNameAndKey(String name, String key) {
        return name + "_" + key;
    }

    public static interface IConfig {

        SharedPreferences getSpImpl(Context c, String name, boolean multiProc);
    }

}
