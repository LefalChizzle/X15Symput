package com.amosgwa.lisukeyboard.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.amosgwa.lisukeyboard.R
import com.amosgwa.lisukeyboard.databinding.LanguagePickerDialogBinding

class LanguagePickerActivity: AppCompatActivity() {
    lateinit var binding: LanguagePickerDialogBinding
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language_picker_dialog)
    }
}