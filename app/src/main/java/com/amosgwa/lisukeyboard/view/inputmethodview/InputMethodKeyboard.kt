package com.amosgwa.lisukeyboard.view.inputmethodview

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.SparseArray
import com.amosgwa.lisukeyboard.keyboardinflater.CustomKey
import com.amosgwa.lisukeyboard.view.keyview.CustomKeyView

class InputMethodKeyboard {
    // Collection of pages
    /**
     * The values are in 2D arrays to keep the rows and keys format.
     * ie : {{Q},{W},{E},{R}...},{{A},{s},{D},{F}...}
     */
    var pages = SparseArray<MutableList<MutableList<CustomKeyView>>>()

    // Create views for each individual keys for a keyboard
    /**
     * Generate key views for each row of keys
     * @param keys These keys are in 2d list.
     */
    fun generateKeyViews(
            context: Context,
            type: Int,
            keys: List<List<CustomKey>>,
            keyboardLanguage: String,
            keyBackground: Int,
            labelPaint: Paint,
            subLabelPaint: Paint,
            isLandscape: Boolean
    ) {
        val keyViews = mutableListOf<MutableList<CustomKeyView>>()
        keys.forEach { rowOfKeys ->
            // Keep track of the row keys.
            val rowKeyViews = mutableListOf<CustomKeyView>()
            // Create row linear layout for the key views.
            rowOfKeys.forEach { key ->
                // The background of the key has to be duplicate since the keys have different widths.
//                val keyBackgroundCopy = keyBackground?.constantState?.newDrawable()?.mutate()
                val keyView = CustomKeyView(
                        context,
                        labelPaint = labelPaint,
                        subLabelPaint = subLabelPaint,
                        key = key,
                        isLandscape = isLandscape,
                        globalKeyBackground = keyBackground
                )
                // Update the language for the key that is assigned with isChange
                if (key.isChangeLanguageKey) {
                    keyView.updateLabel(keyboardLanguage)
                }
                rowKeyViews.add(keyView)
            }

            // Key tracks of the rows with key views.
            keyViews.add(rowKeyViews.toMutableList())
            rowKeyViews.clear()
        }
        pages.append(type, keyViews)
    }

}