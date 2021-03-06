package com.symput.mathkeyboard.data

import android.content.Context
import android.content.SharedPreferences

class KeyboardPreferences(context: Context) {
    private val KEYBOARD_PREFERENCES_NAME = "keyboard_preferences"

    var preference: SharedPreferences

    init {
        preference = context.getSharedPreferences(KEYBOARD_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun putBoolean(key: String, data: Boolean) {
        preference.edit()?.putBoolean(key, data)?.apply()
    }

    fun putInt(key: String, data: Int) {
        preference.edit()?.putInt(key, data)?.apply()
    }

    fun getBoolean(key: String): Boolean {
        return preference.getBoolean(key, false)
    }

    fun getInt(key: String, defaultInt: Int): Int {
        return preference.getInt(key, defaultInt)
    }

    companion object {
        const val KEY_CURRENT_LANGUAGE_IDX = "key_current_language_idx"
        const val KEY_ENABLE_VIBRATION = "key_enable_vibration"
        const val KEY_ENABLE_SOUND = "key_enable_sound"
        const val KEY_NEEDS_RELOAD = "key_needs_reload"
    }
}