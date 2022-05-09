package com.crash.crash_lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常捕获
 * <p>
 * Date: 2022/4/15 13:43
 * Author: liangdp
 */
public class CrashHaldleApplication extends Application {

    protected Context appCtx;
    private Activity currentActivity;

    @IntDef({CrashShowStyle.STYLE_NEW_PAGE,
            CrashShowStyle.STYLE_DIALOG_IF_CAN_SHOW})
    public @interface CrashShowStyle {
        int STYLE_NEW_PAGE = 101;
        int STYLE_DIALOG_IF_CAN_SHOW = 102;
    }

    private int tipShowStyle = CrashShowStyle.STYLE_NEW_PAGE;

    @Override
    public void onCreate() {
        super.onCreate();
        appCtx = getApplicationContext();
        // 监控 未捕获的异常
        setDefaultUncaughtExceptionHandler();
        // 注册activity 生命周期
        registerActivityLifecycle();
        // 捕获 主线程的异常
        catchMainThreadException();
    }

    private void setDefaultUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
                interceptCrash(e);
            }
        });
    }

    private void catchMainThreadException() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Looper.loop();
                    } catch (Throwable t) {
                        interceptCrash(t);
                    }
                }
            }
        });
    }

    private void registerActivityLifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                CrashHaldleApplication.this.currentActivity = activity;
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
            }
        });
    }

    private static final HashMap<String, String> crashMap = new HashMap<>(10);

    private void interceptCrash(Throwable t) {
        try {
            PackageManager packageManager = appCtx.getPackageManager();
            String packageName = appCtx.getPackageName();
            StringBuilder builder = new StringBuilder();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                crashMap.clear();
                crashMap.put("versionName : ", packageInfo.versionName);
                crashMap.put("versionCode : ", packageInfo.versionCode + "");

                Field[] fields = Build.class.getFields();
                if (fields.length > 0) {
                    for (Field field : fields) {
                        field.setAccessible(true);
                        crashMap.put(field.getName(), field.get(null).toString());
                    }
                }



                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                t.printStackTrace(printWriter);

                Throwable cause = t.getCause();
                while (cause != null) {
                    cause.printStackTrace(printWriter);
                    cause = t.getCause();
                }

                printWriter.close();
                String tErrorStr = stringWriter.toString();

                builder.append("\n===========Crash  Log  Begin============\n")
                        .append(tErrorStr).append("\n");

                for (Map.Entry<String,String> entry :crashMap.entrySet()){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    builder.append(key).append(" : ").append(value).append("\n\n");
                }

                builder.append("\n===========Crash  Log   End==============\n");

            }

            String errorStr = builder.toString();


            if (tipShowStyle == CrashShowStyle.STYLE_DIALOG_IF_CAN_SHOW && currentActivity != null
                    && !currentActivity.isFinishing() && !currentActivity.isDestroyed()) {
                AlertDialog alertDialog = new AlertDialog.Builder(currentActivity)
                        .setCancelable(true)
                        .setTitle("crash-info")
                        .setMessage(errorStr)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        })
                        .setNeutralButton("复制所有", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, errorStr));
                                Toast toast = new Toast(appCtx);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setText("已复制到剪切板");
                                toast.show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        }).create();
                alertDialog.show();
            } else {
                CrashTipActivity.openNewPage(appCtx,errorStr);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private final Handler handler = new Handler(Looper.getMainLooper());

    public void setTipShowStyle(@CrashShowStyle int tipShowStyle) {
        this.tipShowStyle = tipShowStyle;
    }
}
