package com.amosgwa.lisukeyboard.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.*
import android.widget.*
import com.amosgwa.lisukeyboard.R
import com.amosgwa.lisukeyboard.keyboard.CustomKey

class CustomKeyView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        var key: CustomKey? = null,
        @ColorInt var globalTextColor: Int? = null,
        var globalTextSize: Float? = null,
        @DrawableRes var globalKeyBackground: Drawable? = ColorDrawable(Color.TRANSPARENT)
) : FrameLayout(context, attrs, defStyleAttr) {

    val repeatable: Boolean? = key?.repeatable
    val codes: IntArray? = key?.codes
    val label: String? = key?.label?.toString()
    val icon: Drawable? = key?.icon
    val isChangeLanguage: Boolean? = key?.isChangeLanguageKey

    private val keyTextView = CustomKeyTextView(
            context,
            color = globalTextColor,
            size = if (key?.textSize == 0.0F) globalTextSize else key?.textSize
    )

    init {
        // Activate the press states on the text views.
        isClickable = true
        // Set the key background.
        background = globalKeyBackground

        // Center the text view inside the FrameLayout and style the text view.
        val childLayoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        childLayoutParams.gravity = Gravity.CENTER
        keyTextView.layoutParams = childLayoutParams

        // Icons get priority in the key view.
        if (icon != null) {
            val imageView = ImageView(context)
            imageView.layoutParams = childLayoutParams
            imageView.setImageDrawable(icon)
            addView(imageView)
        } else {
            keyTextView.text = label
            addView(keyTextView)
        }
    }

    fun updateLabel(newLabel: String) {
        keyTextView.text = newLabel
    }
}

class CustomKeyTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = R.style.CustomKeyDefaultStyle,
        @ColorInt var color: Int? = null,
        var size: Float? = null
) : TextView(context, attrs, defStyleAttr, defStyleRes) {
    init {
        background = ColorDrawable(Color.TRANSPARENT)

        setTextColor(color ?: DEFAULT_TEXT_COLOR)

        textSize = size ?: DEFAULT_TEXT_SIZE
        textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    companion object {
        const val DEFAULT_TEXT_SIZE = 12.0f
        const val DEFAULT_TEXT_COLOR = Color.BLACK
    }
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