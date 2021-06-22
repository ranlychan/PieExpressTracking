package com.ranlychen.pieexpresstracking;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AppInfoActivity extends WearableActivity {
    Intent home;
    TextView localversion;
    TextView qq;
    ImageView wechatpay;
    ImageView alipay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);

        home = new Intent(AppInfoActivity.this,MainActivity.class);
        localversion = findViewById(R.id.aboutpage_localversion);
        qq = findViewById(R.id.aboutpage_qq);
        wechatpay = findViewById(R.id.aboutpage_wechatpay);
        alipay = findViewById(R.id.aboutpage_alipay);

        wechatpay.setVisibility(View.GONE);
        alipay.setVisibility(View.GONE);

        localversion.setText("版本号：" + getLocalVersion(this));
    }

    /**
     * 方法名：getLocalVersion(Context context)
     * 功    能：读取软件本地版本号(VersionName)
     * 参    数：Context context
     * 返回值：String localVersion
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

    public void backhome(View v) {
        finish();
    }
    public void pay(View v) {
        wechatpay.setVisibility(View.VISIBLE);
        alipay.setVisibility(View.VISIBLE);
    }

    private static long lastClickTime=0;
    private static final int CLICK_TIME = 100; //快速点击间隔时间
    private int COUNTS = 6;// 点击次数
    private long[] mHits = new long[COUNTS];//记录点击次数
    private long DURATION = 2000;//有效时间

    public void version(View v){

        //将mHints数组内的所有元素左移一个位置
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //获得当前系统已经启动的时间
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)){
            Toast.makeText(AppInfoActivity.this, R.string.eggText,Toast.LENGTH_SHORT).show();
            mHits = new long[COUNTS];//初始化点击次数
        }
    }
}
