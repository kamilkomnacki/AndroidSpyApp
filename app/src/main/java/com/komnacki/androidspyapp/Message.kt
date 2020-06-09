package com.komnacki.androidspyapp

import android.content.Context

interface Message {
    fun getData(context: Context): Map<String, Any>

}
