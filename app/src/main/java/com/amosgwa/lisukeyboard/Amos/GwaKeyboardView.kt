package com.amosgwa.lisukeyboard.Amos

import android.R.attr.*
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.inputmethodservice.KeyboardView
import android.util.AttributeSet
import android.view.MotionEvent
import com.amosgwa.lisukeyboard.R


/**
 * Created by Amos Gwa on 2/18/2018.
 */

class GwaKeyboardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet
) : KeyboardView(context, attrs) {

    var canvas: Canvas? = null

    override fun onDraw(canvas: Canvas) {
        loadKeyCodes()
        super.onDraw(canvas)
        this.canvas = canvas
        drawCustomKeys(this.canvas)
    }

    override fun onTouchEvent(me: MotionEvent?): Boolean {
        isPreviewEnabled
        return super.onTouchEvent(me)
    }

    private fun drawPopup() {

    }

    private fun drawCustomKeys(canvas: Canvas?) {
        if (keyboard != null) {

            for (i in 0 until keyboard.keys.size) {
                var key = keyboard.keys[i] as GwaKeyboardKey
                if (key.subLabel != null) {
                    val subKeyPaint = Paint()
                    subKeyPaint.color = Color.RED
                    subKeyPaint.textSize = 26f
                    val paddingX = key.width * 0.2
                    val paddingY = key.height * 0.2
                    val startX = (key.width + key.x - paddingX).toFloat()
                    val startY = (paddingY + key.y).toFloat()
                    canvas?.drawText(key.subLabel as String?, startX, startY, subKeyPaint)
                }

                if (key.codes.isNotEmpty() && key.codes[0] == KEYCODE_SPACE) {
                    val dr = resources.getDrawable(R.drawable.ic_space)
                    val width = Math.round(key.width * 0.8).toInt()
                    val height = Math.round(key.height * 0.2).toInt()
                    val startX = (key.width - width) / 2 + key.x
                    val startY = (key.height - height) / 2 + (key.y * 1.03).toInt()
                    val endX = startX + width
                    val endY = startY + height
                    dr.setBounds(startX, startY, endX, endY)
                    dr.draw(canvas)
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
