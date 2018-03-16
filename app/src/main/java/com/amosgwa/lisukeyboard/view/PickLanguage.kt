package com.amosgwa.lisukeyboard.view

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.amosgwa.lisukeyboard.R

import kotlinx.android.synthetic.main.activity_pick_language.*

class PickLanguage : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_language)
    }
}
