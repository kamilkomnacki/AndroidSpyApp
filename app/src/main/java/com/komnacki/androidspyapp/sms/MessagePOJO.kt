package com.komnacki.androidspyapp.sms

import com.google.gson.annotations.SerializedName

class MessagePOJO(
    id: Long,
    threadId: Long,
    addressNumber: String,
    person: String,
    date: Long,
    dateSend: Long,
    protocol: Long,
    read: String,
    status: String,
    type: String,
    subject: String,
    body: String
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
    var date : Long? = date

    @SerializedName("dateSend")
    var dateSend : Long? = dateSend

    @SerializedName("read")
    var read : String? = read

    @SerializedName("protocol")
    var protocol : Long? = protocol

    @SerializedName("status")
    var status : String? = status

    @SerializedName("type")
    var type : String? = type

    @SerializedName("subject")
    var subject : String? = subject

    @SerializedName("body")
    var body : String? = body


    override fun toString() : String {
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
    }
}