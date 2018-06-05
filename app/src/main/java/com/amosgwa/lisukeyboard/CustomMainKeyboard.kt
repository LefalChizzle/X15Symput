package com.amosgwa.lisukeyboard

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.media.AudioManager
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.MotionEvent
import android.content.res.TypedArray
import android.util.Log
import android.util.SparseArray
import com.amosgwa.lisukeyboard.data.KeyboardPreferences
import com.amosgwa.lisukeyboard.keyboard.CustomKeyboard
import com.amosgwa.lisukeyboard.view.CustomKeyboardView


class CustomMainKeyboard : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var customKeyboardView: CustomKeyboardView

    private lateinit var keyboardNormal: CustomKeyboard
    private lateinit var keyboardShift: CustomKeyboard
    private lateinit var keyboardSymbol: CustomKeyboard

    private var languageNames: MutableList<String> = mutableListOf()
    private var languageXmlRes: MutableList<Int> = mutableListOf()
    private var languageShiftXmlRes: MutableList<Int> = mutableListOf()
    private var languageSymbolXmlRes: MutableList<Int> = mutableListOf()

    private var keyboards: SparseArray<CustomKeyboard> = SparseArray()

    private var preferences: KeyboardPreferences? = null
    private var lastSavedLanguageIdx: Int = 0
        set(value) {
            field = value
//            preferences?.putInt(KeyboardPreferences.KEY_CURRENT_LANGUAGE, value)
//            keyboardNormal = GwaKeyboard(applicationContext, languageXmlRes[value], TYPE_NORMAL)
//            keyboardShift = GwaKeyboard(applicationContext, languageShiftXmlRes[value], TYPE_SHIFT)
//            keyboardSymbol = GwaKeyboard(applicationContext, languageSymbolXmlRes[value], TYPE_SYMBOL)
//            keyboardView?.keyboard = keyboardNormal
//            keyboardView?.currentLanguage = languageNames[value]
//            keyboardView?.invalidateAllKeys()
        }

    override fun onCreate() {
        super.onCreate()
        loadKeyCodes()
        loadLanguages()
        loadSharedPreferences()
    }

    private fun loadSharedPreferences() {
//        preferences = KeyboardPreferences(applicationContext)
//        lastSavedLanguageIdx = preferences?.getInt(KeyboardPreferences.KEY_CURRENT_LANGUAGE, 0)
//                ?: 0
    }

    override fun onCreateInputView(): View? {
        keyboardNormal = CustomKeyboard(this, languageXmlRes[lastSavedLanguageIdx], TYPE_NORMAL, languageNames[lastSavedLanguageIdx])
        keyboardShift = CustomKeyboard(this, languageShiftXmlRes[lastSavedLanguageIdx], TYPE_SHIFT, languageNames[lastSavedLanguageIdx])
        keyboardSymbol = CustomKeyboard(this, languageSymbolXmlRes[lastSavedLanguageIdx], TYPE_SYMBOL, languageNames[lastSavedLanguageIdx])

        keyboards.append(TYPE_NORMAL, keyboardNormal)
        keyboards.append(TYPE_SHIFT, keyboardShift)
        keyboards.append(TYPE_SYMBOL, keyboardSymbol)

        customKeyboardView = layoutInflater.inflate(R.layout.keyboard, null) as CustomKeyboardView
        customKeyboardView.keyboardViewListener = this
        customKeyboardView.keyboards = keyboards
        customKeyboardView.currentKeyboardType = 0

        return customKeyboardView
    }

    private fun loadLanguages() {
        val languagesArray = resources.obtainTypedArray(R.array.languages)
        var eachLanguageTypedArray: TypedArray? = null
        for (i in 0 until languagesArray.length()) {
            val id = languagesArray.getResourceId(i, -1)
            if (id == -1) {
                throw IllegalStateException("Invalid language array resource")
            }
            eachLanguageTypedArray = resources.obtainTypedArray(id)
            eachLanguageTypedArray?.let {
                val nameIdx = 0

                val languageName = it.getString(nameIdx)
                val xmlRes = it.getResourceId(RES_IDX, -1)
                val shiftXmlRes = it.getResourceId(SHIFT_IDX, -1)
                val symbolXmlRes = it.getResourceId(SYM_IDX, -1)

                if (languageName == null || xmlRes == -1 || shiftXmlRes == -1 || symbolXmlRes == -1) {
                    throw IllegalStateException("Make sure the arrays resources contain name, xml, and shift xml")
                }

                languageNames.add(languageName)
                languageXmlRes.add(xmlRes)
                languageShiftXmlRes.add(shiftXmlRes)
                languageSymbolXmlRes.add(symbolXmlRes)
            }
        }
        if (eachLanguageTypedArray != null) {
            eachLanguageTypedArray.recycle()
        }
        languagesArray.recycle()
    }

    override fun onPress(primaryCode: Int) {
    }

    override fun onRelease(primaryCode: Int) {
    }

    override fun swipeRight() {
        Log.d("///AMOS", "SWIPE RIGHT")
    }

    override fun swipeLeft() {
        Log.d("///AMOS", "SWIPE LEFT")
    }

    override fun swipeUp() {
        Log.d("///AMOS", "SWIPE UP")
    }

    override fun swipeDown() {
        Log.d("///AMOS", "SWIPE DOWN")
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val inputConnection = currentInputConnection
        playClick(primaryCode)
        when (primaryCode) {
            Keyboard.KEYCODE_DELETE -> {
                val selectedText: CharSequence? = inputConnection.getSelectedText(0)
                if (selectedText == null) {
                    inputConnection.deleteSurroundingText(1, 0)
                } else {
                    if (selectedText.isEmpty()) {
                        inputConnection.deleteSurroundingText(1, 0)
                    } else {
                        inputConnection.commitText("", 1)
                    }
                }
            }
            KEYCODE_ABC -> {
                customKeyboardView.currentKeyboardType = TYPE_NORMAL
                return
            }
            Keyboard.KEYCODE_SHIFT -> {
                customKeyboardView.currentKeyboardType = TYPE_SHIFT
                return
            }
            KEYCODE_UNSHIFT -> {
                customKeyboardView.currentKeyboardType = TYPE_NORMAL
                return
            }
            KEYCODE_123 -> {
                customKeyboardView.currentKeyboardType = TYPE_SYMBOL
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
        if (customKeyboardView.currentKeyboard().type == TYPE_SHIFT) {
            customKeyboardView.currentKeyboardType = TYPE_NORMAL
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
        KEYCODE_123 = resources.getInteger(R.integer.keycode_sym)
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

        const val TYPE_NORMAL = 0
        const val TYPE_SHIFT = 1
        const val TYPE_SYMBOL = 2

        const val RES_IDX = 1
        const val SHIFT_IDX = 2
        const val SYM_IDX = 3
    }
}
