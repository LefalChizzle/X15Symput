package com.amosgwa.lisukeyboard.keyboard

import android.content.Context
import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.inputmethodservice.Keyboard

class CustomKeyboard(context: Context?, xmlLayoutResId: Int, val type: Int, val language: String) : Keyboard(context, xmlLayoutResId) {
    override fun createKeyFromXml(res: Resources?, parent: Row?, x: Int, y: Int, parser: XmlResourceParser?): Key {
        return CustomKey(res, parent, x, y, parser)
    }

    fun getRows(): MutableList<List<CustomKey>> {
        val rows = mutableListOf<List<CustomKey>>()
        var row = mutableListOf<CustomKey>()
        @Suppress("UNCHECKED_CAST")
        (this.keys as MutableList<CustomKey>).forEach { key ->
            row.add(key)
            if (key.isRightEdge()) {
                rows.add(row)
                row = mutableListOf()
            }
        }
        return rows
    }
}
