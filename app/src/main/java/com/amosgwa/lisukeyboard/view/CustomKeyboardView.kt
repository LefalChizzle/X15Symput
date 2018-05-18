package com.amosgwa.lisukeyboard.view

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.widget.LinearLayout
import com.amosgwa.lisukeyboard.R
import com.amosgwa.lisukeyboard.databinding.CustomKeyboardViewBinding
import android.view.*


class CustomKeyboardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnTouchListener {
    private val binding: CustomKeyboardViewBinding = CustomKeyboardViewBinding.inflate(LayoutInflater.from(context), this, true)

    private val renderedKeys = mutableListOf<CustomKeyPreview>()

    var currentX = 0
    var currentY = 0
    //TODO Add setKeyBoard in MainKeyboard

    init {
        this.clipChildren = false
        this.clipToPadding = false
        background = context.getDrawable(R.color.pink)
        setOnTouchListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val pressedKey = CustomKeyPreview(context, height = 100)
                pressedKey.setBackgroundColor(context.resources.getColor(R.color.pink))
                pressedKey.x = currentX
                pressedKey.y = currentY
                currentX += 100
                currentY += 10

                renderPressedKey(pressedKey)
            }
            MotionEvent.ACTION_UP -> {
                clearRenderedKeys()
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderPressedKey(pressedKey: CustomKeyPreview) {
        val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val params = WindowManager.LayoutParams(100, 100,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT)
        params.flags = params.flags or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        params.gravity = Gravity.TOP or Gravity.START
        params.x = pressedKey.x
        params.y = pressedKey.y
        windowManager.addView(pressedKey, params)
        renderedKeys.add(pressedKey)
    }

    private fun clearRenderedKeys() {
        val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        renderedKeys.let {
            renderedKeys.map { keyView -> windowManager.removeView(keyView) }
            renderedKeys.clear()
        }
    }

    companion object {
        var pointers = 0
    }
}