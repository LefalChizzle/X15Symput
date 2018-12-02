package com.amosgwa.lisukeyboard.view.inputmethodview

import android.util.SparseArray
import com.amosgwa.lisukeyboard.common.PageType
import com.amosgwa.lisukeyboard.view.keyview.CustomKeyView

class InputMethodKeyboard {
    // Collection of pages
    var pages = SparseArray<MutableList<MutableList<CustomKeyView>>>()

    /**
     * Type is from [PageType.Companion]. [keys] are in 2D arrays to keep the rows and keys format.
     * ie : {{Q},{W},{E},{R}...},{{A},{s},{D},{F}...}
     */
    fun addKeys(type: Int, keys: MutableList<MutableList<CustomKeyView>>) {
        pages.append(type, keys)
    }
}