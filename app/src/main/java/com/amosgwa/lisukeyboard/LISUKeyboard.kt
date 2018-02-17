package com.amosgwa.lisukeyboard

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.media.AudioManager
import android.view.KeyEvent
import android.view.View

class LISUKeyboard : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private var keyboardView: KeyboardView? = null
    private var keyboardNormal: Keyboard? = null
    private var keyboardShift: Keyboard? = null
    private var keyboard123: Keyboard? = null

    override fun onCreateInputView(): View? {
        loadKeyCodes()

        keyboardNormal = Keyboard(this, R.xml.lisu)
        keyboardShift = Keyboard(this, R.xml.lisu_shift)
        keyboard123 = Keyboard(this, R.xml.lisu_num)

        keyboardView = layoutInflater.inflate(R.layout.keyboard, null) as KeyboardView?
        keyboardView?.keyboard = keyboardNormal

        keyboardView.add

        keyboardView?.setOnKeyboardActionListener(this)
        return keyboardView
    }

    override fun swipeRight() {
    }

    override fun onPress(primaryCode: Int) {
    }

    override fun onRelease(primaryCode: Int) {
    }

    override fun swipeLeft() {
    }

    override fun swipeUp() {
    }

    override fun swipeDown() {
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val inputConnection = currentInputConnection
        playClick(primaryCode)
        when (primaryCode) {
            Keyboard.KEYCODE_DELETE -> {
                inputConnection.deleteSurroundingText(1, 0)
            }
            KEYCODE_ABC -> {
                keyboardView?.keyboard = keyboardNormal
                keyboardView?.invalidateAllKeys()
            }
            Keyboard.KEYCODE_SHIFT -> {
                keyboardView?.keyboard = keyboardShift
                keyboardView?.invalidateAllKeys()
            }
            KEYCODE_UNSHIFT -> {
                keyboardView?.keyboard = keyboardNormal
                keyboardView?.invalidateAllKeys()
            }
            KEYCODE_123 -> {
                keyboardView?.keyboard = keyboard123
                keyboardView?.invalidateAllKeys()
            }
            Keyboard.KEYCODE_DONE -> {
                inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            }
            else -> {
                var code = primaryCode.toChar()
                inputConnection.commitText(code.toString(), 1)
            }
        }
    }

    private fun playClick(keyCode: Int) {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        when (keyCode) {
            32 -> audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR)
            Keyboard.KEYCODE_DONE, 10 -> audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN)
            Keyboard.KEYCODE_DELETE -> audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE)
            else -> audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)
        }
    }

    private fun loadKeyCodes() {
        KEYCODE_UNSHIFT = resources.getInteger(R.integer.keycode_unshift)
        KEYCODE_ABC = resources.getInteger(R.integer.keycode_abc)
        KEYCODE_123 = resources.getInteger(R.integer.keycode_123)
    }

    override fun onText(text: CharSequence?) {
    }

    companion object {
        var KEYCODE_NONE = -777
        var KEYCODE_UNSHIFT = KEYCODE_NONE
        var KEYCODE_ABC = KEYCODE_NONE
        var KEYCODE_123 = KEYCODE_NONE
    }
}
