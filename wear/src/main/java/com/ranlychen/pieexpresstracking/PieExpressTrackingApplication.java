package com.ranlychen.pieexpresstracking;


import android.app.Application;
import android.content.Context;

import com.dbflow5.config.FlowManager;

public class PieExpressTrackingApplication extends Application {

    public static Context gContext;

    private static PieExpressTrackingApplication app;

    public static PieExpressTrackingApplication getInstance() {
        if (app == null){
            app = new PieExpressTrackingApplication();
        }
        return app;
    }

    private void setInstance(PieExpressTrackingApplication application) {
        PieExpressTrackingApplication.app = application;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gContext = getApplicationContext();
        FlowManager.init(gContext);
    }
}
