package com.amosgwa.lisukeyboard.view.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.amosgwa.lisukeyboard.R
import io.flutter.facade.Flutter

class FlutterSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flutter_setting)
        val flutterView = Flutter.createView(
                this,
                lifecycle,
                "setting"
        )
        val frameLayout = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT)
        addContentView(flutterView, frameLayout)
    }
}
