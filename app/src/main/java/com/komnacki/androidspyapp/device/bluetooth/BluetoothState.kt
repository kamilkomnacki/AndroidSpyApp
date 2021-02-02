package com.komnacki.androidspyapp.device.bluetooth

import android.content.Context
import com.komnacki.androidspyapp.results.Message
import github.nisrulz.easydeviceinfo.base.EasyBluetoothMod


class BluetoothState(override var context: Context, var bluetoothName : String) :
    Message {
    init {
        getBluetoothState(
            context,
            bluetoothName
        )
    }

    companion object {
        private val info : MutableMap<String, Any> = mutableMapOf()

        fun getBluetoothState(context: Context, bluetoothName: String) {
            val easyBluetoothState = EasyBluetoothMod(context)
            try {
                info["MAC"] = easyBluetoothState.bluetoothMAC
                info["DISPLAYED_NAME"] = bluetoothName
            } catch (e : Exception) {

            }
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}