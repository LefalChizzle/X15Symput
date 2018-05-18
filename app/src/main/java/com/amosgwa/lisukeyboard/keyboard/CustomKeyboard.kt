package com.amosgwa.lisukeyboard.keyboard

import android.content.Context
import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.inputmethodservice.Keyboard

class CustomKeyboard(context: Context?, xmlLayoutResId: Int, val type: Int) : Keyboard(context, xmlLayoutResId) {
    override fun createKeyFromXml(res: Resources?, parent: Row?, x: Int, y: Int, parser: XmlResourceParser?): Key {
        return CustomKey(res, parent, x, y, parser)
    }
}
