package com.amosgwa.lisukeyboard.data

import android.content.Context
import android.content.SharedPreferences

class KeyboardPreferences(context: Context) {
    private val KEYBOARD_PREFERENCES_NAME = "keyboard_preferences"

    var preference: SharedPreferences? = null

    init {
        preference = context.getSharedPreferences(KEYBOARD_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun putString(key: String, data: String) {
        preference?.edit()?.putString(key, data)?.apply()
    }

    fun putInt(key: String, data: Int) {
        preference?.edit()?.putInt(key, data)?.apply()
    }

    fun getString(key: String, defaultString: String? = null): String? {
        return preference?.getString(key, defaultString)
    }

    fun getInt(key: String, defaultInt: Int): Int {
        return preference?.getInt(key, defaultInt) ?: defaultInt
    }

    companion object {
        const val KEY_CURRENT_LANGUAGE = "keyboard_preferences"
    }
}