package com.kys.knowyourshop;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.kys.knowyourshop.Service.MyLocationService;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by sanniAdewale on 31/01/2017.
 */

public class MyApplication extends Application {
    private static MyApplication sInstance;
    private String TASK_TAG_PERIODIC = "myTask";
    //private static DatabaseDb database;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
        sInstance = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("avenir_light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        MyTask();
        //database = new DatabaseDb(this);
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

//    public synchronized static DatabaseDb getWritableDatabase(){
//        if (database == null){
//            database = new DatabaseDb(getAppContext());
//        }
//        return database;
//
//    }
}
