package com.komnacki.androidspyapp.device.nfc

import android.content.Context
import com.komnacki.androidspyapp.results.Message
import github.nisrulz.easydeviceinfo.base.EasyNfcMod

class NFCState(override var context: Context) :
    Message {
    init {
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