package com.komnacki.androidspyapp.device.battery

import android.content.Context
import com.komnacki.androidspyapp.Message
import github.nisrulz.easydeviceinfo.base.BatteryHealth
import github.nisrulz.easydeviceinfo.base.ChargingVia
import github.nisrulz.easydeviceinfo.base.EasyBatteryMod

//Todo: do wersji API 26:  https://github.com/nisrulz/easydeviceinfo/wiki/Usage#easybluetoothmod
class BatteryState(override var context: Context) :
    Message {
    init {
        getBatteryState(
            context
        )
    }

    companion object {
        private val info : MutableMap<String, Any> = mutableMapOf()

        fun getBatteryState(context: Context) {
            val easyBatteryMod = EasyBatteryMod(context)
            val batteryHealth = when (easyBatteryMod.batteryHealth) {
                BatteryHealth.GOOD -> "Good"
                BatteryHealth.HAVING_ISSUES -> "Having issues"
                else -> "Unknown"
            }

            val chargingSource = if (easyBatteryMod.isDeviceCharging) {
                when (easyBatteryMod.batteryHealth) {
                    ChargingVia.AC -> "AC"
                    ChargingVia.UNKNOWN_SOURCE -> "Unknown"
                    ChargingVia.USB -> "USB"
                    ChargingVia.WIRELESS -> "Wireless"
                    else -> "Unknown"
                }
            } else {
                "Not charge"
            }

            info.put("PERCENTAGE", easyBatteryMod.batteryPercentage)
            info.put("TEMPERATURE_IN_C", easyBatteryMod.batteryTemperature)
            info.put("VOLTAGE_IN_mV", easyBatteryMod.batteryVoltage)
            info.put("TECHNOLOGY", easyBatteryMod.batteryTechnology.toString())
            info.put("IS_CHARGING", easyBatteryMod.isDeviceCharging)
            info.put("CHARGING_SOURCE", chargingSource)
            info.put("HEALTH", batteryHealth)
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}