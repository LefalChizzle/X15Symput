package com.amosgwa.lisukeyboard.view

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.amosgwa.lisukeyboard.R
import com.amosgwa.lisukeyboard.databinding.ActivitySettingBinding
import com.amosgwa.lisukeyboard.extensions.showToast

class SettingActivity : AppCompatActivity() {
    private var binding: ActivitySettingBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkDrawOverlayPermission()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
    }

    fun openLanguageSetting() {
        val intent = Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS)
        startActivity(intent)
    }

    private fun checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps  */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                /** if not construct intent to request permission  */
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName"))
                /** request permission via start activity for result  */
                startActivityForResult(
                        intent,
                        REQUEST_CODE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        /** check if received result code
         * is equal our requested code for draw permission   */
        if (requestCode == Companion.REQUEST_CODE) {
            // ** if so check once again if we have permission */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // continue here - permission was granted
                    showToast("Permission is granted, you may proceed")
                } else {
                    showToast("No Permission is granted, you may not proceed")
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 65535
    }
}
