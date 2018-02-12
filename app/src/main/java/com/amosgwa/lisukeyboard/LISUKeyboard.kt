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

    private var isShift = false

    init {
    }

    override fun onCreateInputView(): View? {
        keyboardNormal = Keyboard(this, R.xml.lisu)
        keyboardShift = Keyboard(this, R.xml.lisu_shift)

        keyboardView = layoutInflater.inflate(R.layout.keyboard, null) as KeyboardView?
        keyboardView?.keyboard = keyboardNormal

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
            Keyboard.KEYCODE_SHIFT -> {
                isShift = !isShift
                if (isShift) {
                    keyboardView?.keyboard = keyboardShift
                } else {
                    keyboardView?.keyboard = keyboardNormal
                }
                keyboardView?.invalidateAllKeys()
            }
            Keyboard.KEYCODE_DONE -> {
                inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            }
            else -> {
                var code = primaryCode.toChar()
                if (Character.isLetter(code) && isShift) {
                    code = Character.toUpperCase(code)
                }
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

    override fun onText(text: CharSequence?) {
    }
}
