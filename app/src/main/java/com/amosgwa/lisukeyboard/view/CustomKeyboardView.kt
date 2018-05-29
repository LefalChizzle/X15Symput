package com.amosgwa.lisukeyboard.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.inputmethodservice.KeyboardView
import android.os.*
import android.support.annotation.RequiresApi
import android.util.*
import android.widget.LinearLayout
import com.amosgwa.lisukeyboard.R
import android.view.*
import com.amosgwa.lisukeyboard.keyboard.CustomKey
import com.amosgwa.lisukeyboard.keyboard.CustomKeyboard
import kotlin.properties.Delegates


class CustomKeyboardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val renderedKeys = mutableListOf<CustomKeyPreview>()
    private val pressedKeys = mutableListOf<CustomKeyView>()

    private lateinit var mHandler: Handler

    var currentX = 0
    var currentY = 0

    var keyTextColor: Int = 0
    var keyTextSize: Float = 0.0f
    var keyBackground: Drawable? = null

    var keyboardViewListener: KeyboardView.OnKeyboardActionListener? = null

    var keyboards: SparseArray<CustomKeyboard> by Delegates.observable(SparseArray()) { _, _, _ ->
        preloadKeyViews()
    }

    var currentKeyboardType: Int by Delegates.observable(0) { _, _, _ ->
        invalidate()
        addKeyViews()
    }

    private var preloadedRowsWithKeyViews = SparseArray<MutableList<MutableList<CustomKeyView>>>()

    fun currentKeyboard(): CustomKeyboard {
        return keyboards[currentKeyboardType]
    }

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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mHandler = Handler()
        val r = Runnable {

        }
    }

    private fun addKeyViews() {
        if (preloadedRowsWithKeyViews[currentKeyboardType].size == childCount) {
            // Use the existing row linear layouts.
            preloadedRowsWithKeyViews[currentKeyboardType].forEachIndexed { idx, row ->
                val rowLinearLayout = getChildAt(idx) as LinearLayout
                rowLinearLayout.removeAllViews()
                row.forEach { key -> rowLinearLayout.addView(key) }
            }
        } else {
            removeAllKeyViews() // Remove parents of the key views.
            removeAllViews() // Remove all of the row Linear Layout
            preloadedRowsWithKeyViews[currentKeyboardType].forEach { row ->
                val rowLinearLayout = LinearLayout(context)
                rowLinearLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                rowLinearLayout.orientation = HORIZONTAL
                rowLinearLayout.gravity = Gravity.CENTER
                row.forEach { key -> rowLinearLayout.addView(key) }
                addView(rowLinearLayout)
            }
        }
    }

    /*
    * Remove the parents of the keys in order to add new keys to the view.
    * */
    private fun removeAllKeyViews() {
        preloadedRowsWithKeyViews[currentKeyboardType].forEach { row ->
            row.forEach { key ->
                val parent = key.parent as ViewGroup?
                parent?.removeView(key)
            }
        }
    }

    private fun getKeyViews(rows: List<List<CustomKey>>): MutableList<MutableList<CustomKeyView>> {
        val keyViews = mutableListOf<MutableList<CustomKeyView>>()
        rows.forEach { row ->
            // Keep track of the row keys.
            val rowKeyViews = mutableListOf<CustomKeyView>()
            // Create row linear layout for the key views.
            row.forEach { key ->
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
            }
            // Key tracks of the rows with key views.
            keyViews.add(rowKeyViews.toMutableList())
            rowKeyViews.clear()
        }
        return keyViews
    }

    private fun preloadKeyViews() {
        for (i in 0 until keyboards.size()) {
            val key = keyboards.keyAt(i)
            keyboards.get(key).getRows().let { rows ->
                val timings = TimingLogger(LOG_TAG, "preloadKeyViews")
                preloadedRowsWithKeyViews.append(key, getKeyViews(rows))
                timings.addSplit("preloadKeyViews")
                timings.dumpToLog()
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                detectRow(event.x, event.y)?.let {
                    val timings = TimingLogger(LOG_TAG, "populateKeyViews")
                    pressedKeys.add(it)
                    timings.addSplit("Action Down")
                    timings.dumpToLog()
                }
                return false
            }
            MotionEvent.ACTION_UP -> {
                sendKey()
                return false
            }
        }
        return false
    }

    /*
    * Key views are stored in 2d format. First, we check if the tap position is within the parent of
    * the key view's bounds. If so, find the key in that row.
    * */
    private fun detectRow(x: Float, y: Float): CustomKeyView? {
        preloadedRowsWithKeyViews[currentKeyboardType].forEach { row ->
            // Each row is composed in linear layout. Thus, we have to use it to find which row
            // the pointer falls into.
            val rowLinearLayout = row.first().parent as LinearLayout?
            rowLinearLayout?.let {
                if (x in rowLinearLayout.left..rowLinearLayout.right &&
                        y in rowLinearLayout.top..rowLinearLayout.bottom) {
                    // Normalize the tap location because the position of the children view are
                    // relative to the parent's.
                    row.forEach { key ->
                        if (x - rowLinearLayout.left in key.left..key.right &&
                                y - rowLinearLayout.top in key.top..key.bottom) {
                            Log.d(LOG_TAG, "pressed : ${key.label}")
                            return key
                        }
                    }
                }
            }
        }
        return null
    }

    private fun sendKey() {
        if (pressedKeys.size == 0) return
        pressedKeys.last().let { key ->
            key.codes?.first()?.let { primaryCode ->
                val timings = TimingLogger("", "populateKeyViews")
                keyboardViewListener?.onKey(primaryCode, key.codes)
                timings.addSplit("Keyboard listener called")
                timings.dumpToLog()
                pressedKeys.clear()
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

        // Message for handler
        fun logTimeDiff(start: Long, end: Long, tag: String) {
            Log.d(tag, "${(end - start) / 1000} micro second")
        }

        const val LOG_TAG = "AMOS"
    }

    interface OnKeyboardActionListener {
        fun onPress()
        fun onKey()
        fun onRelease()
        fun onSwipe()
    }
}