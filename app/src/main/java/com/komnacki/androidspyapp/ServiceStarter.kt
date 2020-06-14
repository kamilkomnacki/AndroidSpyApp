package com.komnacki.androidspyapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

class ServiceStarter : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("KK: SERVICE: ", "onReceive")
        //todo: check co to za akcja
        if (intent != null && intent.action != null) {
            val triggerIntent = Intent()
            if (context != null) {
                val prefs = context.getSharedPreferences(
                    MainActivity.SHARED_PREFERENCE_TAG,
                    Context.MODE_PRIVATE
                )
                val prefsUserEmail = prefs.getString(MainActivity.PREFS_USER_EMAIL, null)
                val prefsUserPassword = prefs.getString(MainActivity.PREFS_USER_PASSWORD, null)
                if (!prefsUserEmail.isNullOrBlank() && !prefsUserPassword.isNullOrBlank()) {
                    triggerIntent.setClass(context, MainService::class.java)
                    context.startService(triggerIntent)
                }
            }
        }
    }
}