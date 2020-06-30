package com.komnacki.androidspyapp.device.fingerprint

import android.content.Context
import com.komnacki.androidspyapp.Message
import github.nisrulz.easydeviceinfo.base.EasyFingerprintMod


class FingerprintState(override var context: Context) :
    Message {
    init {
        getFingerprintState(
            context
        )
    }

    companion object {
        private val info: MutableMap<String, Any> = mutableMapOf()

        fun getFingerprintState(context: Context) {
            val easyFingerprintMod = EasyFingerprintMod(context)
            try {
                info["IS_SENSOR_PRESENT"] = easyFingerprintMod.isFingerprintSensorPresent
                info["IS_ENROLLED"] = easyFingerprintMod.areFingerprintsEnrolled()
            } catch (e : SecurityException) {
                info["IS_SENSOR_PRESENT"] = "Undefined"
                info["IS_ENROLLED"] = "Undefined"
            }
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}