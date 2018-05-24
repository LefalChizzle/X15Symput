package com.amosgwa.lisukeyboard.view

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.inputmethodservice.KeyboardView
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.widget.LinearLayout
import com.amosgwa.lisukeyboard.R
import android.view.*
import android.util.DisplayMetrics
import android.util.Log
import com.amosgwa.lisukeyboard.keyboard.CustomKey
import com.amosgwa.lisukeyboard.keyboard.CustomKeyboard

class CustomKeyboardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val renderedKeys = mutableListOf<CustomKeyPreview>()

    var currentX = 0
    var currentY = 0

    var keyTextColor: Int = 0
    var keyTextSize: Float = 0.0f
    var keyBackground: Drawable? = null

    var keyboardViewListener: KeyboardView.OnKeyboardActionListener? = null
    val firstKeyLocation = IntArray(2)
    val lastKeyLocation = IntArray(2)

    var keyboard: CustomKeyboard? = null
        set(value) {
            // Create key views and add them to this view
            field = value
            populateKeyViews()
            requestLayout()
        }

    private var rowsWithKeyViews = mutableListOf<List<CustomKeyView>>()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomKeyboardView)
        /*
        * Load the styles from the keyboard xml for the child keys. Keyboard should be the only place
        * where we set the styles for the children views.
        * */
        keyTextColor = a.getColor(R.styleable.CustomKeyboardView_keyTextColor, context.getColor(R.color.default_key_text_color))
        keyTextSize = a.getDimension(R.styleable.CustomKeyboardView_keyTextSize, CustomKeyTextView.DEFAULT_TEXT_SIZE)
        keyBackground = a.getDrawable(R.styleable.CustomKeyboardView_keyBackground)

        // recycle the typed array
        a.recycle()

        // Set orientation for the rows
        orientation = VERTICAL
    }

    private fun addKeyViews(rows: List<List<CustomKey>>) {
        for (row in rows) {
            // Keep track of the row keys.
            val rowKeyViews = mutableListOf<CustomKeyView>()
            // Create row linear layout for the key views.
            val rowLinearLayout = LinearLayout(context)
            rowLinearLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            rowLinearLayout.orientation = HORIZONTAL
            rowLinearLayout.gravity = Gravity.CENTER

            for (key in row) {
                // The background of the key has to be duplicate since the keys have different widths.
                val keyBackgroundCopy = keyBackground?.constantState?.newDrawable()?.mutate()
                val keyView = CustomKeyView(
                        context,
                        codes = key.codes,
                        label = key.label?.toString(),
                        icon = key.icon,
                        textColor = keyTextColor,
                        textSize = keyTextSize,
                        keyBackground = keyBackgroundCopy
                )
                val params = LinearLayout.LayoutParams(
                        key.width,
                        key.height
                )
                keyView.layoutParams = params
                // Keeps track of all of the key views
                rowKeyViews.add(keyView)
                rowLinearLayout.addView(rowKeyViews.last())
            }
            // Add the row with keys to this view.
            this.addView(rowLinearLayout)
            // Key tracks of the rows with key views.
            rowsWithKeyViews.add(rowKeyViews)
        }
    }

    private fun populateKeyViews() {
        rowsWithKeyViews.clear()
        keyboard?.let { keyboard ->
            keyboard.getRows().let { rows ->
                addKeyViews(rows)
            }
        }
    }

    /*
    * Returns the size of the display.
    * */
    private fun getDisplayMetrics(): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//        when (event?.action) {
//            MotionEvent.ACTION_DOWN -> {
//                val pressedKey = CustomKeyPreview(context, height = 100)
//                pressedKey.setBackgroundColor(context.resources.getColor(R.color.pink))
//                pressedKey.x = currentX
//                pressedKey.y = currentY
//                currentX += 100
//                currentY += 10
//
//                renderPressedKey(pressedKey)
//            }
//            MotionEvent.ACTION_UP -> {
//                clearRenderedKeys()
//            }
//        }
//        return true
//    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                detectRow(event.x, event.y)
            }
        }
        return false
    }

    private fun detectRow(x: Float, y: Float) {
        for (row in rowsWithKeyViews) {
            // Each row is composed in linear layout. Thus, we have to use it to find which row
            // the pointer falls into.
            val rowLinearLayout = row.first().parent as LinearLayout?
            rowLinearLayout?.let {
                if (x in rowLinearLayout.left..rowLinearLayout.right &&
                        y in rowLinearLayout.top..rowLinearLayout.bottom) {
                    // Normalize the tap location because the position of the children view are
                    // relative to the parent's.
                    for (key in row) {
                        if (x - rowLinearLayout.left in key.left..key.right &&
                                y - rowLinearLayout.top in key.top..key.bottom) {
                            Log.d("///Amos", "pressed : ${key.label}")
                            return
                        }
                    }
                }
            }
        }
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

    interface OnKeyboardActionListener {
        fun onPress()
        fun onKey()
        fun onRelease()
        fun onSwipe()
    }
}