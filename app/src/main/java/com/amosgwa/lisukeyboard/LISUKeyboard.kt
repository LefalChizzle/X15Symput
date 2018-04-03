package com.amosgwa.lisukeyboard

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.media.AudioManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.MotionEvent
import com.amosgwa.lisukeyboard.keyboard.GwaKeyboard
import com.amosgwa.lisukeyboard.keyboard.GwaKeyboardView


class LISUKeyboard : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private var keyboardView: GwaKeyboardView? = null
    private var keyboardNormal: GwaKeyboard? = null
    private var keyboardShift: GwaKeyboard? = null
    private var keyboard123: GwaKeyboard? = null

    override fun onCreateInputView(): View? {
        loadKeyCodes()

        keyboardNormal = GwaKeyboard(this, R.xml.lisu, TYPE_LISU)
        keyboardShift = GwaKeyboard(this, R.xml.lisu_shift, TYPE_SHIFT)
        keyboard123 = GwaKeyboard(this, R.xml.lisu_num, TYPE_123)

        keyboardView = layoutInflater.inflate(R.layout.keyboard, null) as GwaKeyboardView?
        keyboardView?.keyboard = keyboardNormal

        // Disable preview for keyboard
        keyboardView?.isPreviewEnabled = false

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
        Log.d("///AMOS","onKEY");
        val inputConnection = currentInputConnection
        playClick(primaryCode)
        when (primaryCode) {
            Keyboard.KEYCODE_DELETE -> {
                inputConnection.deleteSurroundingText(1, 0)
            }
            KEYCODE_ABC -> {
                keyboardView?.keyboard = keyboardNormal
                keyboardView?.invalidateAllKeys()
                return
            }
            Keyboard.KEYCODE_SHIFT -> {
                keyboardView?.keyboard = keyboardShift
                keyboardView?.invalidateAllKeys()
                return
            }
            KEYCODE_UNSHIFT -> {
                keyboardView?.keyboard = keyboardNormal
                keyboardView?.invalidateAllKeys()
                return
            }
            KEYCODE_123 -> {
                keyboardView?.keyboard = keyboard123
                keyboardView?.invalidateAllKeys()
                return
            }
            KEYCODE_123 -> {
                keyboardView?.keyboard = keyboard123
                keyboardView?.invalidateAllKeys()
                return
            }
            KEYCODE_MYA_TI_MYA_NA -> {
                val output = KEYCODE_MYA_TI.toChar().toString() + KEYCODE_MYA_NA.toChar()
                inputConnection.commitText(output, 2)
            }
            KEYCODE_NA_PO_MYA_NA -> {
                val output = KEYCODE_NA_PO.toChar().toString() + KEYCODE_MYA_NA.toChar()
                inputConnection.commitText(output, 2)
            }
            KEYCODE_LANGUAGE -> {
                val mgr = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                mgr?.showInputMethodPicker()
            }
            Keyboard.KEYCODE_DONE -> {
                val event = (KeyEvent(0, 0, MotionEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_ENTER, 0, 0, 0, 0,
                        KeyEvent.FLAG_SOFT_KEYBOARD))
                inputConnection.sendKeyEvent(event)
            }
            else -> {
                inputConnection.commitText(primaryCode.toChar().toString(), 1)
            }
        }
        // Switch back to normal if the selected page type is shift.
        val keyboard = keyboardView?.keyboard as GwaKeyboard
        if (keyboard.type == TYPE_SHIFT) {
            keyboardView?.keyboard = keyboardNormal
            keyboardView?.invalidateAllKeys()
        }
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("///AMOS","PRINGING LONGGGGG");
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("///AMOS","KEY DOWNNNN");
        return super.onKeyDown(keyCode, event)
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
        KEYCODE_SPACE = resources.getInteger(R.integer.keycode_space)
        KEYCODE_LANGUAGE = resources.getInteger(R.integer.keycode_switch_next_keyboard)
        KEYCODE_NA_PO_MYA_NA = resources.getInteger(R.integer.keycode_na_po_mya_na)
        KEYCODE_MYA_TI_MYA_NA = resources.getInteger(R.integer.keycode_mya_ti_mya_na)
        KEYCODE_MYA_TI = resources.getInteger(R.integer.keycode_mya_ti)
        KEYCODE_MYA_NA = resources.getInteger(R.integer.keycode_mya_na)
        KEYCODE_NA_PO = resources.getInteger(R.integer.keycode_na_po)
    }

    override fun onText(text: CharSequence?) {
    }

    companion object {
        var KEYCODE_NONE = -777
        var KEYCODE_UNSHIFT = KEYCODE_NONE
        var KEYCODE_ABC = KEYCODE_NONE
        var KEYCODE_123 = KEYCODE_NONE
        var KEYCODE_SPACE = KEYCODE_NONE
        var KEYCODE_NA_PO_MYA_NA = KEYCODE_NONE
        var KEYCODE_MYA_TI_MYA_NA = KEYCODE_NONE
        var KEYCODE_LANGUAGE = KEYCODE_NONE
        var KEYCODE_NA_PO = KEYCODE_NONE
        var KEYCODE_MYA_NA = KEYCODE_NONE
        var KEYCODE_MYA_TI = KEYCODE_NONE

        const val TYPE_LISU = 0
        const val TYPE_SHIFT = 1
        const val TYPE_123 = 2
    }
}
