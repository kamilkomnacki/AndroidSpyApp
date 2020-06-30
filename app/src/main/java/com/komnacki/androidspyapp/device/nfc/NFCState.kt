package com.komnacki.androidspyapp.device.nfc

import android.content.Context
import android.util.Log
import com.komnacki.androidspyapp.Message
import github.nisrulz.easydeviceinfo.base.EasyNfcMod

class NFCState(override var context: Context) :
    Message {
    init {
        Log.d("KK: ", "NFCState")
        getNFCkState(
            context
        )
    }

    companion object {
        private val info: MutableMap<String, Any> = mutableMapOf()

        fun getNFCkState(context: Context) {
            val easyNFCState = EasyNfcMod(context)
            info["IS_PRESENT"] = easyNFCState.isNfcPresent
            info["IS_ENABLED"] = easyNFCState.isNfcEnabled
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}