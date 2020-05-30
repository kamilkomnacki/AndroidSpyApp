package com.komnacki.androidspyapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.komnacki.androidspyapp.MainActivity.Companion.mFirebaseDatabaseReference
import github.nisrulz.easydeviceinfo.base.EasyBatteryMod
import github.nisrulz.easydeviceinfo.base.EasyConfigMod
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class MainService : Service() {
    private lateinit var s: Disposable
    private var timer: Long = 0
    private lateinit var easyConfigMod : EasyConfigMod;

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val returnVal = super.onStartCommand(intent, flags, startId)
        Log.d("SERVICE: ", "on onStartCommand()")

        if (!s.isDisposed) {
            s.dispose()
        }
        doWork()
        return returnVal
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d("SERVICE: ", "on lowMemory()")
    }

    override fun stopService(name: Intent?): Boolean {
        Log.d("SERVICE: ", "stopService")
        s.dispose()
        return super.stopService(name)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("SERVICE: ", "on onBind()")
        return null
    }

    override fun onCreate() {
        super.onCreate()
//        easyConfigMod = EasyConfigMod(this)
        Log.d("SERVICE: ", "on create()")
        doWork()
    }

    private fun doWork() {
        s = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { action ->
                timer = action
                if(timer%10 == 0L) {
                    writeNew(easyConfigMod.formattedTime)
                }
                Log.d("SERVICE COUNTER: ", action.toString())
            }
    }

    private fun writeNew(id: String) {
//        val easyBatteryMod = EasyBatteryMod(this)
//        val message = Message(easyBatteryMod.batteryPercentage.toString())
//        mFirebaseDatabaseReference.child("test1").child(id).child("asd").setValue(message)
//        mFirebaseDatabaseReference.child("test1").child(id).child("asd").child("qwe").setValue(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SERVICE: ", "on destroy()")
        if (!s.isDisposed) {
            s.dispose()
        }
    }
}