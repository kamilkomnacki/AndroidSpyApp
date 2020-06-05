package com.komnacki.androidspyapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.common.api.GoogleApi
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
        Log.d("SERVICE: ", "main activity onCreate  ")

        setContentView(R.layout.activity_main)

        var isEmailValid = false
        var isPasswordValid = false
        et_email.addTextChangedListener (object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                isEmailValid = validateEmail(s.toString())
                btn_connect.isEnabled = isEmailValid && isPasswordValid
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        et_password.addTextChangedListener (object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                isPasswordValid = !s.toString().isBlank()
                btn_connect.isEnabled = isEmailValid && isPasswordValid
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val rootView = super.onCreateView(name, context, attrs)

        return rootView
    }

//    override fun onCreateView(
//        parent: View?,
//        name: String,
//        context: Context,
//        attrs: AttributeSet
//    ): View? {
//        val rootView = super.onCreateView(parent, name, context, attrs)
//        setContentView(R.layout.activity_main)
//
//        var isEmailValid = false
//        var isPasswordValid = false
//        et_email.addTextChangedListener (object:TextWatcher{
//            override fun afterTextChanged(s: Editable?) {
//                isEmailValid = validateEmail(s.toString())
//                btn_connect.isEnabled = isEmailValid && isPasswordValid
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//        et_password.addTextChangedListener (object:TextWatcher{
//            override fun afterTextChanged(s: Editable?) {
//                isPasswordValid = !s.toString().isBlank()
//                btn_connect.isEnabled = isEmailValid && isPasswordValid
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//        return rootView
//    }

    fun validateEmail(input: String) : Boolean {
        return input.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))
    }
}