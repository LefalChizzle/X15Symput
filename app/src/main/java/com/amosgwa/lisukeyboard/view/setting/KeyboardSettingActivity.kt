package com.amosgwa.lisukeyboard.view.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.amosgwa.lisukeyboard.R
import com.amosgwa.lisukeyboard.data.KeyboardPreferences
import com.amosgwa.lisukeyboard.data.KeyboardPreferences.Companion.KEY_ENABLE_SOUND
import com.amosgwa.lisukeyboard.data.KeyboardPreferences.Companion.KEY_ENABLE_VIBRATION
import com.amosgwa.lisukeyboard.data.KeyboardPreferences.Companion.KEY_NEEDS_RELOAD
import com.amosgwa.lisukeyboard.databinding.ActivitySettingBinding
import com.amosgwa.lisukeyboard.view.dialog.CustomDialog


class KeyboardSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var preferences: KeyboardPreferences
    private var enabledKeyboard: Boolean = false
    private var shouldFinish: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        preferences = KeyboardPreferences(applicationContext)

        checkKeyboardEnabled()
        setupActions()
    }

    override fun onResume() {
        super.onResume()
        if(shouldFinish){
            return
        }
        checkKeyboardEnabled()
    }

    private fun checkKeyboardEnabled() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).let {
            enabledKeyboard = it.enabledInputMethodList.any { it.packageName == packageName }
            val dialogShown = supportFragmentManager.findFragmentByTag(CustomDialog.TAG) != null
            if (!enabledKeyboard && !dialogShown) {
                // Show setting dialog
                showEnableKeyboardDialog()
                shouldFinish = false
            }

            // Check if the keyboard has been picked
            val imeSelected = Settings.Secure.getString(
                    contentResolver,
                    Settings.Secure.DEFAULT_INPUT_METHOD
            ).contains(packageName)

            if (imeSelected) {
                Toast.makeText(this, "Symput is already selected", Toast.LENGTH_SHORT).show()
            }
            else if (enabledKeyboard && !imeSelected) {
                Handler().postDelayed({ it.showInputMethodPicker() }, 500)
                shouldFinish = true
            }
        }
    }

    private fun showEnableKeyboardDialog() {
        CustomDialog.Builder()
                .title("Enable keyboard")
                .message("Please enable Symput in the settings")
                .positiveText("OK")
                .negativeText("Later")
                .listener(object : CustomDialog.Listener {
                    override fun onPositiveSelect(dialog: CustomDialog) {
                        dialog.dismiss()
                        startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
                    }

                    override fun onNegativeSelect(dialog: CustomDialog) {
                        dialog.dismiss()
                        shouldFinish = true
                    }
                })
                .build()
                .show(supportFragmentManager, CustomDialog.TAG)
    }


    private fun setupActions() {
        binding.chooseButton.setOnClickListener{checkKeyboardEnabled()}

        binding.enableVibration.apply {
            isChecked = preferences.getBoolean(KEY_ENABLE_VIBRATION)
            setOnClickListener {
                isChecked = !isChecked
                preferences.putBoolean(KEY_ENABLE_VIBRATION, isChecked)
                preferences.putBoolean(KEY_NEEDS_RELOAD, true)
            }
        }

        binding.enableSound.apply {
            isChecked = preferences.getBoolean(KEY_ENABLE_SOUND)
            setOnClickListener {
                isChecked = !isChecked
                preferences.putBoolean(KEY_ENABLE_SOUND, isChecked)
                preferences.putBoolean(KEY_NEEDS_RELOAD, true)
            }
        }

        binding.weblink.setOnClickListener {
            try {
                    val uriUrl = Uri.parse(url)
                    val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
                    startActivity(launchBrowser)
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(this, "There is no browser installed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val url = "https://www.symput.com/feedback"
    }
}