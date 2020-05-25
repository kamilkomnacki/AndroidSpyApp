package com.komnacki.androidspyapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

class ServiceStarter : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("SERVICE: ", "onReceive")
        var triggerIntent = Intent()
        if (context != null) {
            triggerIntent.setClass(context, MainService::class.java)
            context.startService(triggerIntent)
        }
    }
}