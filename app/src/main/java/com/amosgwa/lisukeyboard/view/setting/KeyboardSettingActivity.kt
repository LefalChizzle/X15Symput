package com.amosgwa.lisukeyboard.view.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.amosgwa.lisukeyboard.R
import com.amosgwa.lisukeyboard.data.KeyboardPreferences
import com.amosgwa.lisukeyboard.data.KeyboardPreferences.Companion.KEY_ENABLE_SOUND
import com.amosgwa.lisukeyboard.data.KeyboardPreferences.Companion.KEY_ENABLE_VIBRATION
import com.amosgwa.lisukeyboard.data.KeyboardPreferences.Companion.KEY_NEEDS_RELOAD
import com.amosgwa.lisukeyboard.databinding.ActivitySettingBinding


class KeyboardSettingActivity : Activity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var preferences: KeyboardPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        preferences = KeyboardPreferences(applicationContext)

        setupActions()
    }

    private fun setupActions() {
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

        binding.email.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)

            emailIntent.data = Uri.parse("mailto:")
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[Lisu Keyboard Android] Feedback")

            try {
                startActivity(Intent.createChooser(emailIntent, "Send feedback..."))
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val email = "gwamosi@gmail.com"
    }
}