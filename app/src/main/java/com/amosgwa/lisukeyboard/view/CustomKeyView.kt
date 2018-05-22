package com.amosgwa.lisukeyboard.view

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import com.amosgwa.lisukeyboard.R

class CustomKeyView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        var codes: IntArray? = null,
        @ColorInt var textColor: Int? = null,
        @DrawableRes var background: Int? = null
) : FrameLayout(context, attrs, defStyleAttr) {
    private val keyTextView = CustomKeyTextView(context)

    init {
        codes?.first()?.let {
            keyTextView.text = it.toChar().toString()
            keyTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            addView(keyTextView)
        }
//        setOnTouchListener(this)
    }

    fun showPopUpPreview() {

    }

//    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

    // Add margin top and bottom to the divider view to overlap over flight items.
//        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        val marginOffset = -keyTextView.height
//        params.setMargins(0, marginOffset, 0, 0)
//
//        when (event?.action) {
//            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> {
//                val previewTextView = CustomKeyPreview(context, height = keyTextView.height * 2)
//                previewTextView.text = text
//                addView(previewTextView, params)
//                return true
//            }
//            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
//                if (childCount > 0) {
//                    removeViewAt(childCount - 1)
//                }
//                return true
//            }
//        }
//        Log.d("///Amos", text.toString())
//        event?.let { pointer ->
//            val viewHitRect = Rect()
//            getHitRect(viewHitRect)
//            if (viewHitRect.contains(pointer.x.toInt(), pointer.y.toInt())) {
//                if (pointerId == -1) {
//                    pointerId = pointer.getPointerId(pointer.actionIndex)
//                    val previewTextView = CustomKeyPreview(context, height = keyTextView.height * 2)
//                    previewTextView.text = text
//                    addView(previewTextView, params)
//                    return true
//                } else {
//                    if (pointerId == pointer.getPointerId(pointer.actionIndex)) {
//
//                    }
//                }
//            }
//        }
//        return false
//    }
}

class CustomKeyTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = R.style.CustomKeyDefaultStyle
) : TextView(context, attrs, defStyleAttr, defStyleRes) {

}

class CustomKeyPreview @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = R.style.CustomKeyPreviewDefaultStyle, height: Int
) : TextView(context, attrs, defStyleAttr, defStyleRes) {
    var x = 0
    var y = 0

    init {
        this.height = height
    }
}