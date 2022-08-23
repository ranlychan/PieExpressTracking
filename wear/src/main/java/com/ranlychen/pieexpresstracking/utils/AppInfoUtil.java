package com.ranlychen.pieexpresstracking.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * @description 应用信息
 * @author chencanyi
 * @time 2022/8/23 14:10
 */
public class AppInfoUtil {

    private static final String TAG = "AppInfoUtil";

    /**
     * @description 读取软件本地版本号(VersionName)
     * @param context
     * @return localVersion
     */
    public static String getLocalVersion(Context context) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    @TargetApi(3)
    public static String getProcessName(Context mContext) {
        final String DEFAULT_PROCESS_NAME = "null_name";
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) mContext
                    .getSystemService(Context.ACTIVITY_SERVICE);
            if (mActivityManager == null) {
                Log.e(TAG, "ActivityManager got null!");
                return DEFAULT_PROCESS_NAME;
            }
            List<ActivityManager.RunningAppProcessInfo> infoList =
                    mActivityManager.getRunningAppProcesses();
            if (infoList == null) {
                Log.e(TAG, "getRunningAppProcesses got null!");
                return DEFAULT_PROCESS_NAME;
            }
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    if (TextUtils.isEmpty(appProcess.processName)) {
                        return DEFAULT_PROCESS_NAME;
                    } else {
                        return appProcess.processName;
                    }
                }
            }
            return DEFAULT_PROCESS_NAME;
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
        }
        return DEFAULT_PROCESS_NAME;
    }
}
