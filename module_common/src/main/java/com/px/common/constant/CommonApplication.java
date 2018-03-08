package com.px.common.constant;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.px.common.crash.CrashHandler;
import com.px.common.utils.Logger;

/**
 * common application
 */

public class CommonApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Logger.init("----px----");
        CrashHandler.getInstance().init();
        boolean isDebug = true;
        if(isDebug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    public static Context getContext() {
        return context;
    }
}
