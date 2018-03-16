package com.amosgwa.lisukeyboard.Amos

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.os.Build
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import com.amosgwa.lisukeyboard.R
import android.view.WindowManager
import com.amosgwa.lisukeyboard.LanguagePickerDialog
import com.amosgwa.lisukeyboard.view.PickLanguage
import android.R.attr.y
import android.R.attr.x
import android.os.SystemClock
import android.widget.Toast


/**
 * Created by Amos Gwa on 2/18/2018.
 */

class GwaKeyboardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet
) : KeyboardView(context, attrs) {

    var canvas: Canvas = Canvas()

    override fun onDraw(canvas: Canvas) {
        loadKeyCodes()
        super.onDraw(canvas)

        this.canvas = canvas
        drawSubKeys(this.canvas)
    }

    override fun onTouchEvent(me: MotionEvent?): Boolean {
        isPreviewEnabled
        return super.onTouchEvent(me)
    }

    override fun onLongPress(popupKey: Keyboard.Key?): Boolean {
        val primaryCode = popupKey?.codes?.get(0)
        when (primaryCode) {
            KEYCODE_SPACE -> {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(context.getString(R.string.language_picker_title))
                builder.setItems(R.array.languages, DialogInterface.OnClickListener { dialog, item ->
                    // Do something with the selection
                    Toast.makeText(context,String.format("You picked %s", item), Toast.LENGTH_SHORT).show()
                })
                val alert = builder.create()
                val window = alert.window
                val lp = window.attributes
                lp.token = windowToken
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
                }
                window.setFormat(PixelFormat.OPAQUE)
                window.setDimAmount(0.5F)
                window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM or
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                alert.show()
                return false
            }
        }
        return super.onLongPress(popupKey)
    }

    private fun drawSubKeys(canvas: Canvas?) {
        if (keyboard != null) {
            for (i in 0 until keyboard.keys.size) {
                var key = keyboard.keys[i] as GwaKeyboardKey
                if (key.subLabel != null) {
                    val subKeyPaint = Paint()
                    subKeyPaint.color = resources.getColor(R.color.subKeyTextColor)
                    subKeyPaint.textSize = 30f
                    val paddingX = key.width * 0.35
                    val paddingY = key.height * 0.35
                    val startX = (key.width + key.x - paddingX).toFloat()
                    val startY = (paddingY + key.y).toFloat()
                    canvas?.drawText(key.subLabel as String?, startX, startY, subKeyPaint)
                }
            }
        }
    }


    private fun loadKeyCodes() {
        KEYCODE_SPACE = resources.getInteger(R.integer.keycode_space)
    }

    companion object {
        var KEYCODE_NONE = -777
        var KEYCODE_SPACE = KEYCODE_NONE
    }
}

