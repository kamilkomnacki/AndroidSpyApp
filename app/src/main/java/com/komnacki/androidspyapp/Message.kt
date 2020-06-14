package com.komnacki.androidspyapp

import android.content.Context

interface Message {
    var context: Context

    fun getData(): Map<String, Any>
}
