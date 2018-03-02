package com.amosgwa.lisukeyboard.Amos

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.inputmethodservice.KeyboardView
import android.util.AttributeSet
import android.view.MotionEvent
import com.amosgwa.lisukeyboard.R
import android.R.attr.y
import android.R.attr.x



/**
 * Created by Amos Gwa on 2/18/2018.
 */

class GwaKeyboardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet
) : KeyboardView(context, attrs) {

    var canvas: Canvas? = null

    override fun onDraw(canvas: Canvas) {
        this.canvas = canvas
        loadKeyCodes()
        super.onDraw(canvas)
        drawCustomKeys(canvas)
    }

    override fun onTouchEvent(me: MotionEvent?): Boolean {
        isPreviewEnabled
        return super.onTouchEvent(me)
    }

    private fun drawPopup() {

    }

    private fun drawCustomKeys(canvas: Canvas) {
        if (keyboard != null) {
//            val paint = Paint()
//            paint.textAlign = Paint.Align.CENTER
//            paint.textSize = 25f
//            paint.color = Color.RED

            for (key in keyboard.keys) {
                if(key.label != null) {
                    val subKeyPaint = Paint()
//                    canvas.drawPaint(subKeyPaint)
                    subKeyPaint.color = Color.RED
//                    subKeyPaint.textAlign = Paint.Align.CENTER
                    subKeyPaint.textSize = 26f
                    val paddingX = key.width * 0.2
                    val paddingY = key.height * 0.2
                    val startX = (key.width + key.x - paddingX).toFloat()
                    val startY = (paddingY + key.y).toFloat()
//                    canvas.drawText(key.label as String?, startX, startY, subKeyPaint)
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
