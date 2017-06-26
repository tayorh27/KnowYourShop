package com.kys.knowyourshop;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.crashlytics.android.Crashlytics;
import com.devs.acr.AutoErrorReporter;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Database.DatabaseDb;
import com.kys.knowyourshop.Information.User;
import com.kys.knowyourshop.Service.MyLocationService;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Logger;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by sanniAdewale on 31/01/2017.
 */

public class MyApplication extends Application {
    private static MyApplication sInstance;
    private String TASK_TAG_PERIODIC = "myTask";
    private static DatabaseDb database;
    AppData data;
    User user;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        TypefaceProvider.registerDefaultIconSets();
        sInstance = this;
        data = new AppData(getAppContext());
        user = data.getUser();
        logUser();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("avenir_light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        MyTask();
        database = new DatabaseDb(this);
//        AutoErrorReporter.get(this)
//                .setEmailAddresses("gisanrinadetayo@gmail.com")
//                .setEmailSubject("Auto Crash Report")
//                .start();
    }

    private void MyTask() {
        GcmNetworkManager gcmNetworkManager = GcmNetworkManager.getInstance(getAppContext());
        PeriodicTask task = new PeriodicTask.Builder()
                .setService(MyLocationService.class)
                .setTag(TASK_TAG_PERIODIC)
                .setPeriod(300L)
                .build();
        gcmNetworkManager.schedule(task);
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public synchronized static DatabaseDb getWritableDatabase() {
        if (database == null) {
            database = new DatabaseDb(getAppContext());
        }
        return database;

    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier(user.username);
        Crashlytics.setUserEmail(user.email);
        Crashlytics.setUserName(user.username);
    }
}
