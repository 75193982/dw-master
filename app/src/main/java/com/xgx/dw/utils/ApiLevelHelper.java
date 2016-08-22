package com.xgx.dw.utils;

import android.os.Build;

/**
 * Created by Administrator on 2016/8/13.
 */
public class ApiLevelHelper {
    public static boolean isAtLeast(int paramInt) {
        return Build.VERSION.SDK_INT >= paramInt;
    }

    public static boolean isLowerThan(int paramInt) {
        return Build.VERSION.SDK_INT < paramInt;
    }
}
