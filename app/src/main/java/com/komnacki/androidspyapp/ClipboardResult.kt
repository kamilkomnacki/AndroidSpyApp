package com.komnacki.androidspyapp

import android.content.Context
import android.util.Log

class ClipboardResult(override var context: Context) : Message {
    init {
        getClipboardContent()
    }

    private lateinit var info: MutableMap<String, Any>

    private fun getClipboardContent() {
        try {
//            var clipboard: ClipboardManager = sys
//            info = mutableMapOf()
//            info["CLIPBOARD"] = device.address
        } catch (e: Exception) {
            Log.e(
                "KK: ",
                "EXCEPTION during save fields for bluetooth in BluetoothScanResult: " + e.cause + ", " + e.message
            )

        }

            Log.e(
                "KK: ",
                "EXCEPTION during save fields for wifi in BluetoothScanResult: " + "Scan result == null"
            )
        }


        override fun getData(): Map<String, Any> {
            return info
        }
    }
