package com.amosgwa.lisukeyboard.keyboard

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.os.Build
import android.util.AttributeSet
import com.amosgwa.lisukeyboard.R
import android.view.WindowManager
import android.graphics.Typeface
import android.util.Log
import com.amosgwa.lisukeyboard.BuildConfig

/**
 * Created by Amos Gwa on 2/18/2018.
 */

class GwaKeyboardView constructor(
        context: Context,
        attrs: AttributeSet
) : KeyboardView(context, attrs) {

    private var canvas: Canvas = Canvas()
    private var textPaint = Paint()
    private val baseTextSize = 30f

    var currentLanguage: String = ""
    var languages: List<String> = mutableListOf()
    var onLanguageSelectionListener: OnLanguageSelectionListener? = null

    init {
        textPaint.isAntiAlias = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        loadKeyCodes()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (BuildConfig.DEBUG) {
            count++
            Log.d("///AMOS", "Drawing" + count.toString())
        }

        this.canvas = canvas
        drawSubKeys(this.canvas)
        drawLanguageOnSpaceBar(this.canvas)
    }

    override fun onLongPress(popupKey: Keyboard.Key?): Boolean {
        val primaryCode = popupKey?.codes?.get(0)
        when (primaryCode) {
            KEYCODE_SPACE -> {
                showLanguagePicker()
                return true
            }
        }
        return super.onLongPress(popupKey)
    }

    private fun showLanguagePicker() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.language_picker_title))
        builder.setItems(languages.toTypedArray(), { _, itemIdx ->
            onLanguageSelectionListener?.onLanguageSelected(itemIdx)
        })

        // Create the dialog that overlays over the keyboard.
        val alert = builder.create()
        val window = alert.window
        val lp = window.attributes
        lp.token = windowToken
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            lp.type = WindowManager.LayoutParams.TYPE_PHONE
        }

        window.setFormat(PixelFormat.OPAQUE)
        window.setDimAmount(0.5F)
        window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM or
                WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        alert.show()
    }

    private fun drawSubKeys(canvas: Canvas) {
        if (keyboard != null) {
            for (i in 0 until keyboard.keys.size) {
                val key = keyboard.keys[i] as GwaKeyboardKey
                if (key.subLabel != null) {
                    val subKeyPaint = textPaint
                    subKeyPaint.color = resources.getColor(R.color.subKeyTextColor)
                    subKeyPaint.typeface = Typeface.DEFAULT
                    subKeyPaint.textSize = baseTextSize
                    val paddingX = key.width * resources.getFraction(R.fraction.sub_key_padding_x_fraction, 1, 1)
                    val paddingY = key.height * resources.getFraction(R.fraction.sub_key_padding_y_fraction, 1, 1)
                    val startX = key.width + key.x - paddingX
                    val startY = paddingY + key.y
                    canvas.drawText(key.subLabel as String?, startX, startY, subKeyPaint)
                }
            }
        }
    }

    private fun drawLanguageOnSpaceBar(canvas: Canvas) {
        keyboard?.let {
            for (i in 0 until keyboard.keys.size) {
                val key = keyboard.keys[i] as GwaKeyboardKey
                if (key.codes.isNotEmpty() && key.codes[0] == KEYCODE_SPACE) {
                    val languagePaint = textPaint
                    languagePaint.color = resources.getColor(R.color.languageTextColor)
                    languagePaint.typeface = Typeface.DEFAULT
                    languagePaint.textSize = baseTextSize * 1.5f
                    languagePaint.textAlign = Paint.Align.CENTER
                    val startX = key.x.toFloat() + key.width * 0.5f
                    val startY = key.y.toFloat() + key.height * 0.7f
                    canvas.drawText(
                            currentLanguage,
                            startX,
                            startY,
                            languagePaint)
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
        var count = 0
    }
}

interface OnLanguageSelectionListener {
    fun onLanguageSelected(languageIdx: Int)
}
