package com.symput.mathkeyboard.view.inputmethodview

import android.content.Context
import android.util.SparseArray
import com.symput.mathkeyboard.keyboardinflater.CustomKey
import com.symput.mathkeyboard.view.keyview.CustomKeyView

class InputMethodKeyboard {
    /**
     * The values are in 2D arrays to keep the rows and keys format.
     * ie : {{Q},{W},{E},{R}...},{{A},{s},{D},{F}...}
     */
    var pages = SparseArray<MutableList<MutableList<CustomKeyView>>>()

    /**
     * Generate key views for each row of keys
     * @param keys These keys are in 2d list.
     */
    fun generateKeyViews(
            context: Context,
            type: Int,
            keys: List<List<CustomKey>>,
            keyboardLanguage: String,
            isLandscape: Boolean
    ) {
        val keyViews = mutableListOf<MutableList<CustomKeyView>>()
        keys.forEach { rowOfKeys ->
            // Keep track of the row keys.
            val rowKeyViews = mutableListOf<CustomKeyView>()
            // Create row linear layout for the key views.
            rowOfKeys.forEach { key ->
                val keyView = CustomKeyView(
                        context,
                        key = key,
                        isLandscape = isLandscape
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