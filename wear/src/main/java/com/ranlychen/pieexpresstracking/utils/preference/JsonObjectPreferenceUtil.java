package com.ranlychen.pieexpresstracking.utils.preference;

import android.util.Log;

import com.google.gson.internal.$Gson$Types;
import com.ranlychen.pieexpresstracking.PieExpressTrackingApplication;
import com.ranlychen.pieexpresstracking.utils.JsonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class JsonObjectPreferenceUtil<T> extends Preference<T> {

    private static final String TAG = "JsonObjectPreference";

    public JsonObjectPreferenceUtil(T defaultValue) {
        super(defaultValue);
    }

    public JsonObjectPreferenceUtil(T defaultValue, String mark) {
        super(defaultValue, mark);
    }

    public JsonObjectPreferenceUtil(T defaultValue, String mark, PropertySet<T> propertySet) {
        super(defaultValue, mark, propertySet);
    }

    @Override
    protected T getConfigValue(Config config, String mark, T defaultValue) {
        String json = config.getString(mark, "");
        if (json == null || "".equals(json)) {
            return defaultValue;
        }
        T returnValue;
        try {
            returnValue = JsonUtils.<T>parseJson(json, this.getSuperclassTypeParameter(this.getClass()));
        } catch (Exception e) {
            returnValue = defaultValue;
            Log.e(TAG, "method->getConfigValue,error reason: " + e.getMessage());
        }
        Log.d(TAG, "method->getConfigValue,json content: " + json);
        return returnValue;
    }


    /**
     * 强制刷新
     * @param value
     */
    public void update(T value) {
        updateConfig(Config.getInstance(PieExpressTrackingApplication.getInstance().gContext), mMark, value);
    }

    @Override
    protected void updateConfig(Config config, String mark, T value) {
        String json = JsonUtils.toJson(value);
        config.setString(mark, json);
    }

    public Boolean delete() {
        return deleteConfig(Config.getInstance(PieExpressTrackingApplication.getInstance().gContext), mMark);
    }

    protected Boolean deleteConfig(Config config, String mark) {
        return config.clearAsync(mark);
    }

    private Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type type = parameterized.getRawType();
        if (type.equals(JsonObjectPreferenceUtil.class)) {
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        } else {
            return getSuperclassTypeParameter(subclass.getSuperclass());
        }
    }
}
