package com.amosgwa.lisukeyboard.view

import android.content.Context
import android.graphics.PixelFormat
import android.inputmethodservice.Keyboard
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.widget.LinearLayout
import com.amosgwa.lisukeyboard.R
import android.view.*
import android.util.DisplayMetrics
import com.amosgwa.lisukeyboard.keyboard.CustomKey
import com.amosgwa.lisukeyboard.keyboard.CustomKeyboard
import com.amosgwa.lisukeyboard.keyboard.CustomRow


class CustomKeyboardView @JvmOverloads constructor(
        context: Context,
        var keyboard: CustomKeyboard? = null,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnTouchListener {
//    private val binding: CustomKeyboardViewBinding = CustomKeyboardViewBinding.inflate(LayoutInflater.from(context), this, true)

    private val renderedKeys = mutableListOf<CustomKeyPreview>()

    var currentX = 0
    var currentY = 0

    /*
    * Load the styles from the keyboard xml for the child keys. Keyboard should be the only place
    * where we set the styles for the children views.
    * */
    private val a = context.obtainStyledAttributes(attrs, R.styleable.CustomKeyboardView)
    private var keyTextColor = a.getColor(R.styleable.CustomKeyboardView_keyTextColor, context.getColor(R.color.custom_keyboard_blue_dark))
    private var keyBackground = a.getResourceId(R.styleable.CustomKeyboardView_keyBackground, context.getColor(R.color.custom_keyboard_grey_medium))

    private var keys = mutableListOf<List<CustomKey>>()
    private var keyViews = mutableListOf<CustomKeyView>()

    init {
        // recycle the typed array
        a.recycle()

        // Set orientation for the rows
        orientation = VERTICAL

        // Create key views and add them to this view
        CustomKeyboard.rows.let {
            populateKeyViews(it)
        }

        // Set listener to the keyboard
        setOnTouchListener(this)
    }

    private fun populateKeyViews(rows: List<CustomRow>) {
        for (row in rows) {
            val rowLinearLayout = LinearLayout(context)
            rowLinearLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            rowLinearLayout.orientation = HORIZONTAL
            if (row.keys == null) return
            for (key in row.keys) {
                val keyView = CustomKeyView(
                        context,
                        codes = key.codes,
                        textColor = keyTextColor,
                        background = keyBackground
                )
                keyView.layoutParams = LinearLayout.LayoutParams(
                        0,
                        key.height,
                        1.0f
                )
                // Keeps track of all of the key views
                keyViews.add(keyView)
                rowLinearLayout.addView(keyViews.last())
            }
            this.addView(rowLinearLayout)
        }
    }

    private fun populateRowsAndKeys(keyboard: Keyboard) {
        var width = 0
        var currentRow = mutableListOf<CustomKey>()
        for (key in keyboard.keys) {
            // New rows are determined by comparing the total width with current key widths.
            if (width < key.width) {
                if (currentRow.size != 0) {
                    keys.add(currentRow)
                }
                currentRow = mutableListOf()
                width = keyboard.minWidth
            }
            currentRow.add(key as CustomKey)
            width -= key.width
        }
    }

    private fun populateKeyViews() {
        for (row in keys) {
            val rowLinearLayout = LinearLayout(context)
            rowLinearLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            rowLinearLayout.orientation = HORIZONTAL
            for (key in row) {
                val keyView = CustomKeyView(
                        context,
                        codes = key.codes,
                        textColor = keyTextColor,
                        background = keyBackground
                )
                keyView.layoutParams = LinearLayout.LayoutParams(
                        0,
                        key.height,
                        1.0f
                )
                // Keeps track of all of the key views
                keyViews.add(keyView)
                rowLinearLayout.addView(keyViews.last())
            }
            this.addView(rowLinearLayout)
        }
    }

    private fun addKeyViews(keyboard: Keyboard) {
        var width = 0
        var rowLinearLayout: LinearLayout? = null
        for (key in keyboard.keys) {
            val keyView = CustomKeyView(
                    context,
                    codes = key.codes,
                    textColor = keyTextColor,
                    background = keyBackground
            )
            // New rows are determined by comparing the total width with current key widths.
            if (width < key.width) {
                rowLinearLayout?.let {
                    this.addView(it)
                }
                // Create a row linear layout.
                rowLinearLayout = LinearLayout(context)
                rowLinearLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                rowLinearLayout.orientation = HORIZONTAL
                width = keyboard.minWidth
            }
            // Add the keyTextView to the row linear layout.
            rowLinearLayout?.let {
                keyView.layoutParams = LinearLayout.LayoutParams(
                        0,
                        key.height,
                        1.0f
                )
                // Keeps track of all of the key views
                keyViews.add(keyView)
                rowLinearLayout.addView(keyViews.last())
            }
            width -= key.width
        }
    }

    private fun getDisplayMetrics(): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
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

    interface OnKeyboardActionListener {
        fun onPress()
        fun onKey()
        fun onRelease()
        fun onSwipe()
    }
}