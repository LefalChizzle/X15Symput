package com.amosgwa.lisukeyboard.view.keyview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.annotation.ColorRes
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.amosgwa.lisukeyboard.R
import com.amosgwa.lisukeyboard.keyboardinflater.CustomKey


class KeyView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = R.style.CustomKeyDefaultStyle,
        val key: CustomKey,
        private val labelPaint: Paint,
        private val subLabelPaint: Paint,
        @ColorRes val normalStateColorRes: Int,
        @ColorRes val pressStateColorRes: Int
) : View(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
) {

    private var normalBackgroundColor: Int = resources.getColor(normalStateColorRes, null)
    private var pressedBackgroundColor: Int = resources.getColor(pressStateColorRes, null)

    private var isKeyPressed: Boolean = false
    var label = key.label as String?
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCustomKeys(canvas)
    }

    override fun onTouchEvent(me: MotionEvent?): Boolean {
        when (me?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isKeyPressed = true
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                // Construct a rect of the view's bounds
                val rect = Rect(left, top, right, bottom)
                if (!rect.contains(left + me.x.toInt(), top + me.y.toInt())) {
                    // User moved outside bounds
                    invalidate()
                    isKeyPressed = false
                }
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                isKeyPressed = false
                invalidate()
            }
        }
        return true
    }

    private fun drawCustomKeys(canvas: Canvas) {
        // Draw canvas background
        canvas.drawColor(if (isKeyPressed) pressedBackgroundColor else normalBackgroundColor)

        // Draw the label on the key
        label?.let {
            drawCenter(canvas, labelPaint, it)
        }

        // Draw the sub label on the key
        if (key.subLabel != null) {
            val paddingX = key.width * 0.35
            val paddingY = key.height * 0.35
            val startX = (key.width + key.x - paddingX).toFloat()
            val startY = (paddingY + key.y).toFloat()
            canvas.drawText(key.subLabel as String, startX, startY, subLabelPaint)
        }
    }

    private fun drawCenter(canvas: Canvas, paint: Paint, text: String) {
        val xPos = canvas.width / 2.0F
        val yPos = (canvas.height / 2.0F - (paint.descent() + paint.ascent()) / 2)
        //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.
        canvas.drawText(text, xPos, yPos, paint)
    }
}