package com.symput.mathkeyboard.keyboardinflater

import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.inputmethodservice.Keyboard
import android.util.Xml
import com.symput.mathkeyboard.R

class CustomKey(val res: Resources?, parent: Keyboard.Row?, x: Int, y: Int, parser: XmlResourceParser?) : Keyboard.Key(res, parent, x, y, parser) {
    var subLabel: CharSequence?
    var isChangeLanguageKey: Boolean = false
    var textSize: Float?

    init {
        val a = res?.obtainAttributes(Xml.asAttributeSet(parser), R.styleable.CustomKey)
        subLabel = a?.getText(R.styleable.CustomKey_subLabel)
        a?.getBoolean(R.styleable.CustomKey_isChangeLanguageKey, false)?.let {
            isChangeLanguageKey = it
        }
        textSize = a?.getDimension(R.styleable.CustomKey_keyTextSize, 0.0F)
        a?.recycle()
    }

    fun isRightEdge(): Boolean {
        return edgeFlags and Keyboard.EDGE_RIGHT == Keyboard.EDGE_RIGHT
    }

    fun isLeftEdge(): Boolean {
        return edgeFlags and Keyboard.EDGE_LEFT == Keyboard.EDGE_LEFT
    }

    fun isEdge(): Boolean {
        return isRightEdge() || isLeftEdge()
    }
}