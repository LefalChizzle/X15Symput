package com.amosgwa.lisukeyboard

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.View

class LISUKeyboard : InputMethodService(), KeyboardView.OnKeyboardActionListener {
    var keyboardView : KeyboardView? = null
    var keyboard : Keyboard? = null

    override fun onCreateInputView(): View? {
        keyboardView = layoutInflater.inflate(R.layout.keyboard, null) as KeyboardView?;
        keyboard = Keyboard(this, R.xml.qwerty)

        keyboardView?.keyboard = keyboard
        keyboardView?.setOnKeyboardActionListener(this)
        return keyboardView
    }

    override fun swipeRight() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPress(primaryCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRelease(primaryCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun swipeLeft() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun swipeUp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun swipeDown() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onText(text: CharSequence?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
