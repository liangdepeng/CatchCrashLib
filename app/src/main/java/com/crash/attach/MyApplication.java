package com.crash.attach;

import android.app.Application;

import com.crash.crash_lib.CrashHaldleApplication;
import com.crash.crash_lib.CrashHandleUtil;

/**
 * 类作用描述
 * <p>
 * Date: 2022/4/18 18:01
 * Author: liangdp
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandleUtil.init(this,CrashHandleUtil.CrashShowStyle.STYLE_NEW_PAGE);
    }
}
