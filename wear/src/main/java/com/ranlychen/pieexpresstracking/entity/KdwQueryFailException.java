package com.ranlychen.pieexpresstracking.entity;


public class KdwQueryFailException extends Exception{
    public static final String DEFAULT_MSG = "查询失败";

    public KdwQueryFailException() {
        super(DEFAULT_MSG);
    }

    public KdwQueryFailException(String message) {
        super(message);
    }
}
