package com.ranlychen.pieexpresstracking.utils.preference;


import com.ranlychen.pieexpresstracking.PieExpressTrackingApplication;

public abstract class Preference<T> {

    private T mDefaultValue;
    private T mValue;

    protected String mMark;

    public Preference(T defaultValue) {
        this(defaultValue, "");
    }

    public Preference(T defaultValue, String mark) {
        this(defaultValue, mark, new PropertySet<>(defaultValue, defaultValue));
    }

    public Preference(T defaultValue, String mark, PropertySet<T> propertySet) {
        mMark = mark;
        mDefaultValue = defaultValue;
        syncSet(mDefaultValue);

        init(defaultValue, mark);
    }

    public boolean isDefault() {
        return isEquals(mValue, mDefaultValue);
    }

    public void reset() {
        set(mDefaultValue);
    }

    public synchronized T get() {
        return mValue;
    }

    public void notifyValueSet(T oldValue, T newValue) {
        // //使用EventBus
        // mPropertySet.oldValue = oldValue;
        // mPropertySet.newValue = newValue;
        // sendNotify(mPropertySet);
    }
    //
    // protected <N> void sendNotify(final N notify) {
    //     //使用EventBus
    //     ThreadUtils.runAsyncOnAvailableThread(new Runnable() {
    //         @Override
    //         public void run() {
    //             EventBus.getDefault().post(notify);
    //         }
    //     });
    // }

    protected boolean isEquals(T oldValue, T newValue) {
        return oldValue == null && newValue == null
                || oldValue != null && oldValue.equals(newValue);
    }

    private synchronized void syncSet(T value) {
        mValue = value;
    }

    protected abstract T getConfigValue(Config config, String mark, T defaultValue);

    protected abstract void updateConfig(Config config, String mark, T value);

    public boolean set(T value) {
        if (doSet(value)) {
            updateConfig(getConfig(), mMark, value);
            return true;
        } else {
            return false;
        }
    }

    private boolean doSet(T value) {
        if (isEquals(mValue, value)) {
            return false;
        } else {
            T oldValue = mValue;
            syncSet(value);
            notifyValueSet(oldValue, value);
            return true;
        }
    }

    protected void init(T defaultValue, String mark) {
        doSet(getConfigValue(getConfig(), mark, defaultValue));
    }

    private Config getConfig() {
        return Config.getInstance(PieExpressTrackingApplication.getInstance().gContext);
    }
}
