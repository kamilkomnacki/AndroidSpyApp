package com.komnacki.androidspyapp

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SERVICE: ", "main activity onCreate")
        setContentView(R.layout.activity_main)

        val receiverFilter = IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED)
        val receiver = ServiceStarter()
        registerReceiver(receiver, receiverFilter)
    }
}
