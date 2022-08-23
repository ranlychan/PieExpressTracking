package com.ranlychen.pieexpresstracking.utils.preference;


public class PropertySet<T> {

    public T oldValue;
    public T newValue;

    public PropertySet(T oldValue, T newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public PropertySet() {
    }
}
