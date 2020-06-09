package com.komnacki.androidspyapp

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import github.nisrulz.easydeviceinfo.base.EasyConfigMod
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class MainService : Service() {
    private var timer: Long = 0
    private lateinit var easyConfigMod: EasyConfigMod
    private lateinit var userEmail: String
    private lateinit var userPassword: String

    private var s: Disposable? = null

    private val BATTERY_DATABASE_TAG = "BATTERY"

    override fun onCreate() {
        super.onCreate()
        Log.d("SERVICE: ", "on create()")

        val prefs = getSharedPreferences(MainActivity.SHARED_PREFERENCE_TAG, Context.MODE_PRIVATE)
        val prefsUserEmail = prefs.getString(MainActivity.PREFS_USER_EMAIL, null)
        val prefsUserPassword = prefs.getString(MainActivity.PREFS_USER_PASSWORD, null)
        if (!prefsUserEmail.isNullOrBlank() && !prefsUserPassword.isNullOrBlank()) {
            userEmail = prefsUserEmail
            userPassword = prefsUserPassword
        }
        easyConfigMod = EasyConfigMod(this)
        FirebaseDatabase.getInstance().reference.child(userEmail).keepSynced(true)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val returnVal = super.onStartCommand(intent, flags, startId)
        Log.d("SERVICE: ", "on onStartCommand()")

        doWork()
        return returnVal
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d("SERVICE: ", "on lowMemory()")
    }

    override fun stopService(name: Intent?): Boolean {
        Log.d("SERVICE: ", "stopService")
        dispose(s)
        return super.stopService(name)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("SERVICE: ", "on onBind()")
        return null
    }

    private fun doWork() {
        if(s == null || s!!.isDisposed) {
            s = Observable.interval(0, 1000*15, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ action ->
                    writeNew()
                }, { t ->
                    Log.e("ERROR: ", t.toString())
                })
        }
    }

    private fun writeNew() {
        Log.d("KK: ", "writeNew!")
        val messageUtils = MessageUtils.getInstance(this, userEmail)
        messageUtils.sendBatteryMessage()
    }

    private fun dispose(disposable: Disposable?) {
        if (disposable != null) {
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
    }
}