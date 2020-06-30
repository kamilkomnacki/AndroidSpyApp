package com.komnacki.androidspyapp.device.memory

import android.content.Context
import android.util.Log
import com.komnacki.androidspyapp.Message
import github.nisrulz.easydeviceinfo.base.EasyMemoryMod


class MemoryState(override var context: Context) :
    Message {
    init {
        Log.d("KK: ", "MemoryState")
        getMemoryState(
            context
        )
    }

    companion object {
        private val info : MutableMap<String, Any> = mutableMapOf()

        fun getMemoryState(context: Context) {
            val easyMemoryMod = EasyMemoryMod(context)
            info["TOTAL_RAM_MB"] = easyMemoryMod.convertToMb(easyMemoryMod.totalRAM)
            info["TOTAL_INTERNAL_MEMORY_MB"] = easyMemoryMod.convertToMb(easyMemoryMod.totalInternalMemorySize)
            info["TOTAL_EXTERNAL_MEMORY_MB"] = easyMemoryMod.convertToMb(easyMemoryMod.totalExternalMemorySize)
            info["AVAILABLE_INTERNAL_MEMORY_MB"] = easyMemoryMod.convertToMb(easyMemoryMod.availableInternalMemorySize)
            info["AVAILABLE_EXTERNAL_MEMORY_MB"] = easyMemoryMod.convertToMb(easyMemoryMod.availableExternalMemorySize)
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}