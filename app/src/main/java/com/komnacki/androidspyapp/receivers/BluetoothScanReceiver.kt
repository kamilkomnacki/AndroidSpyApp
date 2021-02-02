package com.komnacki.androidspyapp.receivers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.komnacki.androidspyapp.MessageUtils
import com.komnacki.androidspyapp.results.BluetoothScanResult

class BluetoothScanReceiver(private val onResultsReceived: (items: List<BluetoothScanResult>) -> Unit) : BroadcastReceiver() {
    var state: MessageUtils.StateChange = MessageUtils.StateChange.CHANGED

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("KK: ", "Bluetooth scan on receive")

        intent?.let {
            if (it.action == BluetoothDevice.ACTION_FOUND) {
                val device: BluetoothDevice? = it.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                context?.let {context ->
                    onResultsReceived.invoke(listOf(
                        BluetoothScanResult(
                            context,
                            device
                        )
                    ))
                    context.unregisterReceiver(this)
                    if (state == MessageUtils.StateChange.NOT_CHANGED) {
                        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                        bluetoothAdapter.disable()
                    }
                }
            }
        }
    }
}