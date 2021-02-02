package com.komnacki.androidspyapp.sms

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

//TODO: Handle all message data
class MessagePOJO(
    id: Long,
    threadId: Long,
    addressNumber: String,
    person: String,
    date: Long
//    dateSend: Long,
//    protocol: Long,
//    read: String,
//    status: String,
//    type: String,
//    subject: String,
//    body: String
) {

    @SerializedName("id")
    var id : Long? = id

    @SerializedName("threadId")
    var threadId : Long? = threadId

    @SerializedName("addressNumber")
    var addressNumber : String? = addressNumber

    @SerializedName("person")
    var person : String? = person

    @SerializedName("date")
    var date : String? = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)

    /*@SerializedName("dateSend")
    var dateSend : Long? = dateSend

    @SerializedName("read")
    var read : String? = when(read) {
        "1" -> "Read"
        else -> "Not read"
    }

    @SerializedName("protocol")
    var protocol : String? = when(protocol.toInt()) {
        -1 -> "Unknown"
        0 -> "SMS Photo"
        1 -> "MMS Photo"
        else -> "Unknown"
    }

    @SerializedName("status")
    var status : String? = when(status) {
        "-1" -> "None"
        "0" -> "Complete"
        "32" -> "Pending"
        "64" -> "Failed"
        else -> "Unknown"
    }

    @SerializedName("type")
    var type : String? = when(type) {
        "0" -> "All"
        "1" -> "Inbox"
        "2" -> "Sent"
        "3" -> "Draft"
        "4" -> "Outbox"
        "5" -> "Failed"
        "6" -> "Queued"
        else -> "Unknown"
    }

    @SerializedName("subject")
    var subject : String? = subject

    @SerializedName("body")
    var body : String? = body*/


/*    override fun toString() : String {
        return "[id = $id," +
                " threadId = $threadId," +
                " addressNumber = $addressNumber," +
                " person = $person," +
                " date = $date, " +
                " dateSend = $dateSend," +
                " protocol = $protocol" +
                " read = $read" +
                " dateSend = $dateSend" +
                " status = $status" +
                " type = $type" +
                " subject = $subject" +
                " body = $body"
    }*/
}