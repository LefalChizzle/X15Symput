package com.amosgwa.lisukeyboard.keyboard

import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.inputmethodservice.Keyboard
import android.util.Xml
import com.amosgwa.lisukeyboard.R

class CustomKey(res: Resources?, parent: Keyboard.Row?, x: Int, y: Int, parser: XmlResourceParser?) : Keyboard.Key(res, parent, x, y, parser) {
    var subLabel: CharSequence?
    var isChangeLanguageKey: Boolean?

    init {
        val a = res?.obtainAttributes(Xml.asAttributeSet(parser), R.styleable.CustomKey)
        subLabel = a?.getText(R.styleable.CustomKey_subLabel)
        isChangeLanguageKey = a?.getBoolean(R.styleable.CustomKey_isChangeLanguageKey, false)
        a?.recycle()
    }
}