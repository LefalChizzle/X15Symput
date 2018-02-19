package com.amosgwa.lisukeyboard.KeyboardView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.util.AttributeSet
import com.amosgwa.lisukeyboard.R

/**
 * Created by Amos Gwa on 2/18/2018.
 */

class GwaKeyboardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet
) : KeyboardView(context, attrs) {

    override fun onDraw(canvas: Canvas) {
        loadKeyCodes()

        super.onDraw(canvas)
        drawCustomKeys(canvas, keyboard)
    }

    private fun drawCustomKeys(canvas: Canvas, keyboard: Keyboard?) {
        if (keyboard != null) {
            val keys = keyboard.keys

            val paint = Paint()
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 25f
            paint.color = Color.RED

            for (key in keys) {
                if (key.codes.isNotEmpty() && key.codes[0] == KEYCODE_SPACE) {
                    val dr = resources.getDrawable(R.drawable.ic_space)
                    val width = Math.round(key.width * 0.8).toInt()
                    val height = Math.round(key.height * 0.25).toInt()
                    val startX = (key.width - width) / 2 + key.x
                    val startY = (key.height - height) / 2 + key.y
                    val endX = startX + width
                    val endY = startY + height
                    dr.setBounds(startX, startY, endX, endY)
                    dr.draw(canvas)
//                    key.icon = ScaleDrawable(resources.getDrawable(R.drawable.ic_space), 0, width.toFloat(), height.toFloat()).drawable
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
