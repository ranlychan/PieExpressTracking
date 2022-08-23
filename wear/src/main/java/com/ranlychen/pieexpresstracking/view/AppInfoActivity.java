package com.ranlychen.pieexpresstracking.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ranlychen.pieexpresstracking.R;
import com.ranlychen.pieexpresstracking.utils.AppInfoUtil;

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

        localversion.setText("版本号：" + AppInfoUtil.getLocalVersion(this));
    }

    public void onBackHomeClick(View v) {
        finish();
    }
    public void onPayClick(View v) {
        wechatpay.setVisibility(View.VISIBLE);
        alipay.setVisibility(View.VISIBLE);
    }

    private static long lastClickTime=0;
    private static final int CLICK_TIME = 100; //快速点击间隔时间
    private int COUNTS = 6;// 点击次数
    private long[] mHits = new long[COUNTS];//记录点击次数
    private long DURATION = 2000;//有效时间

    public void onVersionClick(View v){

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
