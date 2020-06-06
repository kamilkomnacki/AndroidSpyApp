package com.komnacki.androidspyapp

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.komnacki.androidspyapp.api.PermissionsService.Companion.TAG
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val MESSAGES_CHILD = "messages"
    companion object{
        lateinit var mFirebaseDatabaseReference : DatabaseReference
        private lateinit var auth: FirebaseAuth

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

        btn_connect.setOnClickListener {
            if(btn_connect.isEnabled) {
                val email = et_email.text.toString()
                val password = et_password.text.toString()
                setButtonVisibility(false)
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            setButtonVisibility(true)
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("KK: ", "signInWithEmail:success")
                            val user = auth.currentUser
                        } else {
                            setButtonVisibility(true)
                            // If sign in fails, display a message to the user.
                            Log.w("KK: ", "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        auth = FirebaseAuth.getInstance()
    }

    private fun setButtonVisibility(isBtnVisible: Boolean) {
        btn_connect.visibility = if(isBtnVisible) View.VISIBLE else View.GONE
        progressBar.visibility = if(isBtnVisible) View.GONE else View.VISIBLE
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val rootView = super.onCreateView(name, context, attrs)

        return rootView
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Log.d(TAG, "onStart: currentUser: " + currentUser.toString())
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