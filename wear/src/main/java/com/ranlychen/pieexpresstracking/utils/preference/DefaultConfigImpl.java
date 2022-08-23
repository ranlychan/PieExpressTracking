package com.ranlychen.pieexpresstracking.utils.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class DefaultConfigImpl implements Config.IConfig {
    public SharedPreferences getSpImpl(Context c, String name, boolean multiProc) {
        return c.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
}
