package com.komnacki.androidspyapp.device.config

import android.content.Context
import com.komnacki.androidspyapp.results.Message
import github.nisrulz.easydeviceinfo.base.EasyConfigMod
import github.nisrulz.easydeviceinfo.base.RingerMode


class ConfigState(override var context: Context) :
    Message {
    init {
        getConfigState(
            context
        )
    }

    companion object {
        private val info : MutableMap<String, Any> = mutableMapOf()

        fun getConfigState(context: Context) {
            val easyConfigMod = EasyConfigMod(context)
            info["RINGER_MODE"] = when(easyConfigMod.deviceRingerMode) {
                RingerMode.NORMAL -> "NORMAL"
                RingerMode.SILENT -> "SILENT"
                RingerMode.VIBRATE -> "VIBRATE"
                else -> "Unknown"
            }
            info["HAS_SD"] = easyConfigMod.hasSdCard()
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}