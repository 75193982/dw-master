package com.xgx.dw.base;

/**
 * Created by Administrator on 2018/3/29.
 */

public class EventCenter<T> {
    private T data;
    private int eventCode;
    public static int COUNTY_SAVE = 1;
    public static int TAIQU_SAVE = 2;
    public static int COUNTY_SELECT = 3;
    public static int PRICE_SAVE = 4;

    public EventCenter(int eventCode) {
        this(eventCode, null);
    }

    public EventCenter(int eventCode, T data) {
        this.eventCode = -1;
        this.eventCode = eventCode;
        this.data = data;
    }

    public int getEventCode() {
        return this.eventCode;
    }

    public T getData() {
        return this.data;
    }
}
