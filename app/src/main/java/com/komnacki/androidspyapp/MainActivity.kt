package com.komnacki.androidspyapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.Time
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.komnacki.androidspyapp.api.PermissionsService.Companion.TAG
import kotlinx.android.synthetic.main.activity_main.*
import java.security.AuthProvider
import java.security.Timestamp
import java.util.*


class MainActivity : AppCompatActivity() {
    private val MESSAGES_CHILD = "messages"
    companion object{
        lateinit var mFirebaseDatabaseReference : DatabaseReference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AppCompat)
        setContentView(R.layout.activity_main)
        Log.d("SERVICE: ", "main activity onCreate  ")

//        val receiverFilter = IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED)
//        val receiver = ServiceStarter()
//        registerReceiver(receiver, receiverFilter)

        //FIREBASE:
//        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        val rootView = super.onCreateView(parent, name, context, attrs)
//        et_email.addTextChangedListener (object:TextWatcher{
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
        return rootView
    }
}