package com.amosgwa.lisukeyboard.keyboardinflater

import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.inputmethodservice.Keyboard


class CustomRow(res: Resources?, parent: Keyboard, parser: XmlResourceParser?, val keys: MutableList<CustomKey> = mutableListOf()) : Keyboard.Row(res, parent, parser) {
}