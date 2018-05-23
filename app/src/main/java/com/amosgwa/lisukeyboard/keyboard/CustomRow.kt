package com.amosgwa.lisukeyboard.keyboard

import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.inputmethodservice.Keyboard
import android.util.Xml
import com.amosgwa.lisukeyboard.R


class CustomRow(res: Resources?, parent: Keyboard, parser: XmlResourceParser?, val keys: MutableList<CustomKey> = mutableListOf()) : Keyboard.Row(res, parent, parser) {
}