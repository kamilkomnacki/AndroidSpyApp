package com.komnacki.androidspyapp.device.location

import android.content.Context
import com.komnacki.androidspyapp.results.Message
import github.nisrulz.easydeviceinfo.base.EasyLocationMod


class LocationState(override var context: Context) :
    Message {
    init {
        getLocationState(
            context
        )
    }

    companion object {
        private val info: MutableMap<String, Any> = mutableMapOf()

        fun getLocationState(context: Context) {
            val easyLocationMod = EasyLocationMod(context)
            try {
                info["LATITUDE"] = easyLocationMod.latLong[0]
                info["LONGITUDE"] = easyLocationMod.latLong[1]
            } catch (e : SecurityException) {
                info["LATITUDE"] = -1
                info["LONGITUDE"] = -1
            }
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}