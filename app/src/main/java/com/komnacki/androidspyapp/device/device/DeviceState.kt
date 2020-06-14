package com.komnacki.androidspyapp.device.device

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.komnacki.androidspyapp.Message
import github.nisrulz.easydeviceinfo.base.EasyDeviceMod
import github.nisrulz.easydeviceinfo.base.NetworkType
import java.text.SimpleDateFormat
import java.util.*


class DeviceState(override var context: Context) :
    Message {
    init {
        getDeviceState(
            context
        )
    }

    companion object {
        private val info: MutableMap<String, Any> = mutableMapOf()

        fun getDeviceState(context: Context) {
            val easyDeviceMod = EasyDeviceMod(context)
//                info["IMEI"] = easyDeviceMod.imei
                info["BUILD_VERSION_CODENAME"] = easyDeviceMod.buildVersionCodename
                info["BUILD_VERSION_INCREMENTAL"] = easyDeviceMod.buildVersionIncremental
                info["BUILD_VERSION_SDK"] = easyDeviceMod.buildVersionSDK
                info["BUILD_ID"] = easyDeviceMod.buildID
                info["MANUFACTURER"] = easyDeviceMod.manufacturer
                info["MODEL"] = easyDeviceMod.model
                info["OS_CODENAME"] = easyDeviceMod.osCodename
                info["OS_VERSION"] = easyDeviceMod.osVersion
//                info["VERSION_CODENAME"] = easyDeviceMod.phoneNo
                info["RADIO_VERSION"] = easyDeviceMod.radioVer
                info["PRODUCT"] = easyDeviceMod.product
                info["DEVICE"] = easyDeviceMod.device
//                info["ORIENTATION"] = easyDeviceMod.getOrientation()
//                info["DEVICE_TYPE"] = easyDeviceMod.getDeviceType()

                info["BOARD"] = easyDeviceMod.board
                info["HARDWARE"] = easyDeviceMod.hardware
                info["BOOTLOADER"] = easyDeviceMod.bootloader
                info["FINGERPRINT"] = easyDeviceMod.fingerprint
                info["IS_ROOTED"] = easyDeviceMod.isDeviceRooted
                info["BUILD_BRAND"] = easyDeviceMod.buildBrand
                info["BUILD_HOST"] = easyDeviceMod.buildHost
                info["BUILD_TAGS"] = easyDeviceMod.buildTags
                info["BUILD_TIME"] = SimpleDateFormat("yyyy-MM-dd").format(easyDeviceMod.buildTime).toString()
                info["BUILD_VERSION_RELEASE"] = easyDeviceMod.buildVersionRelease
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}