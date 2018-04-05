package com.amosgwa.lisukeyboard.view

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import com.amosgwa.lisukeyboard.R
import com.amosgwa.lisukeyboard.databinding.ActivitySettingBinding
import android.view.MotionEvent
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager


class SettingActivity : Activity() {
    var binding: ActivitySettingBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        showKeyboard()
    }

    override fun onStart() {
        super.onStart()
        showKeyboard()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        showKeyboard()
    }

    override fun onResume() {
        super.onResume()
        showKeyboard()
    }

    fun showKeyboard() {
        binding?.editField?.postDelayed(Runnable {
            binding?.editField?.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding?.editField, InputMethodManager.SHOW_IMPLICIT);
//            binding?.editField?.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0))
//            binding?.editField?.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0))
        }, 900)
    }
}
