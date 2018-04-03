package com.amosgwa.lisukeyboard.view

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.amosgwa.lisukeyboard.R
import com.amosgwa.lisukeyboard.databinding.ActivitySettingBinding

class SettingActivity : Activity() {
    var binding: ActivitySettingBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
    }
}
