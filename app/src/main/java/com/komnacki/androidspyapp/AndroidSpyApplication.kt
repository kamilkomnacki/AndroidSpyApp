package com.komnacki.androidspyapp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.multidex.MultiDex
import com.google.firebase.database.FirebaseDatabase


class AndroidSpyApplication : Application(), Application.ActivityLifecycleCallbacks {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    override fun onCreate() {
        super.onCreate()
        Log.d("KK: ", "onCreate")
        registerActivityLifecycleCallbacks(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    override fun onLowMemory() {
        Log.d("KK: ", "onLowMemory")
        super.onLowMemory()
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d("KK: ", "onActivityPaused")

    }

    override fun onActivityStarted(activity: Activity) {
        Log.d("KK: ", "onActivityStarted")

    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d("KK: ", "onActivityDestroyed")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.d("KK: ", "onActivitySaveInstanceState")

    }

    override fun onActivityStopped(activity: Activity) {
        Log.d("KK: ", "onActivityStopped")

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d("KK: ", "onActivityCreated")

    }

    override fun onActivityResumed(activity: Activity) {
        Log.d("KK: ", "onActivityResumed")

    }
}