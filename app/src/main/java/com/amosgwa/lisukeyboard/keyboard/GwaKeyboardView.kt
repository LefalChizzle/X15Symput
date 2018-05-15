package com.amosgwa.lisukeyboard.keyboard

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.*
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.util.AttributeSet
import com.amosgwa.lisukeyboard.R
import android.util.Log
import com.amosgwa.lisukeyboard.BuildConfig
import android.os.Build
import android.view.*
import com.amosgwa.lisukeyboard.databinding.LanguagePickerDialogBinding
import com.amosgwa.lisukeyboard.view.LanguagePickerActivity


/**
 * Created by Amos Gwa on 2/18/2018.
 */

class GwaKeyboardView constructor(
        context: Context,
        attrs: AttributeSet
) : KeyboardView(context, attrs) {

    private var canvas: Canvas = Canvas()
    private var textPaint = Paint()
    private val baseTextSize = 30f

    var alert: CustomDialog? = null

    var currentLanguage: String = ""
    var languages: List<String> = mutableListOf()
    var onLanguageSelectionListener: OnLanguageSelectionListener? = null

    init {
        textPaint.isAntiAlias = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        loadKeyCodes()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (BuildConfig.DEBUG) {
            count++
            Log.d("///AMOS", "Drawing" + count.toString())
        }

        this.canvas = canvas
        drawSubKeys(this.canvas)
        drawLanguageOnSpaceBar(this.canvas)
    }

    override fun onLongPress(popupKey: Keyboard.Key?): Boolean {
        val primaryCode = popupKey?.codes?.get(0)
        when (primaryCode) {
            KEYCODE_SPACE -> {
                showLanguagePicker()
                return true
            }
        }
        return super.onLongPress(popupKey)
    }

    private fun startLanguagePickerDialogActivity() {
        val dialogIntent = Intent(context, LanguagePickerActivity::class.java)
        context.startActivity(dialogIntent)
    }

    private fun createLanguagePickerDialog(): CustomDialog {
        val dialog = CustomDialog(context)
        dialog.setTitle("Change language")
        val hovering = false
        val showing = false
        val window = dialog.window
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
        dialog.setCanceledOnTouchOutside(true)
        val res = context.resources
        val lp = window.attributes
        lp.token = windowToken
        lp.format = PixelFormat.TRANSLUCENT
        lp.title = "Choose Language"
        lp.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        lp.windowAnimations = -1
//        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            lp.type = WindowManager.LayoutParams.TYPE_PHONE
        }
//
//
        val dialogBinding = DataBindingUtil.inflate<LanguagePickerDialogBinding>(LayoutInflater.from(context), R.layout.language_picker_dialog, null, false)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.root.setOnClickListener(OnClickListener {
            Log.d("///Dialog","Tapped")
            dialog.dismiss()
        })
//        dialog.setContentView(R.layout.language_picker_dialog)
        return dialog
//
//        val mDialogView = dialog.findViewById(R.id.volume_dialog) as ViewGroup
//        mDialogView.setOnHoverListener(object : View.OnHoverListener() {
//            fun onHover(v: View, event: MotionEvent): Boolean {
//                val action = event.actionMasked
//                hovering = action == MotionEvent.ACTION_HOVER_ENTER || action == MotionEvent.ACTION_HOVER_MOVE
//                rescheduleTimeoutH()
//                return true
//            }
//        })
//        mDialogContentView = dialog.findViewById(R.id.volume_dialog_content)
//        mDialogRowsView = mDialogContentView.findViewById(R.id.volume_dialog_rows)
//        mExpanded = false
//        mExpandButton = mDialogView.findViewById(R.id.volume_expand_button) as ImageButton
//        mExpandButton.setOnClickListener(mClickExpand)
//        mExpandButton.setVisibility(
//                if (AudioSystem.isSingleVolume(context)) View.GONE else View.VISIBLE)
//        updateWindowWidthH()
//        updateExpandButtonH()
//        mMotion = VolumeDialogMotion(dialog, mDialogView, mDialogContentView, mExpandButton,
//                object : VolumeDialogMotion.Callback() {
//                    fun onAnimatingChanged(animating: Boolean) {
//                        if (animating) return
//                        if (mPendingStateChanged) {
//                            mHandler.sendEmptyMessage(H.STATE_CHANGED)
//                            mPendingStateChanged = false
//                        }
//                        if (mPendingRecheckAll) {
//                            mHandler.sendEmptyMessage(H.RECHECK_ALL)
//                            mPendingRecheckAll = false
//                        }
//                    }
//                })
//        if (mRows.isEmpty()) {
//            addRow(AudioManager.STREAM_MUSIC,
//                    R.drawable.ic_volume_media, R.drawable.ic_volume_media_mute, true)
//            if (!AudioSystem.isSingleVolume(context)) {
//                addRow(AudioManager.STREAM_RING,
//                        R.drawable.ic_volume_ringer, R.drawable.ic_volume_ringer_mute, true)
//                addRow(AudioManager.STREAM_ALARM,
//                        R.drawable.ic_volume_alarm, R.drawable.ic_volume_alarm_mute, false)
//                addRow(AudioManager.STREAM_VOICE_CALL,
//                        R.drawable.ic_volume_voice, R.drawable.ic_volume_voice, false)
//                addRow(AudioManager.STREAM_BLUETOOTH_SCO,
//                        R.drawable.ic_volume_bt_sco, R.drawable.ic_volume_bt_sco, false)
//                addRow(AudioManager.STREAM_SYSTEM,
//                        R.drawable.ic_volume_system, R.drawable.ic_volume_system_mute, false)
//                addRow(AudioManager.STREAM_ACCESSIBILITY, R.drawable.ic_volume_accessibility,
//                        R.drawable.ic_volume_accessibility, true)
//            }
//        } else {
//            addExistingRows()
//        }
//        mExpandButtonAnimationDuration = res.getInteger(R.integer.volume_expand_animation_duration)
//        mZenFooter = dialog.findViewById(R.id.volume_zen_footer) as ZenFooter
//        mZenFooter.init(mZenModeController)
//        mZenPanel = dialog.findViewById(R.id.tuner_zen_mode_panel) as TunerZenModePanel
//        mZenPanel.init(mZenModeController)
//        mZenPanel.setCallback(mZenPanelCallback)
    }


    private fun showLanguagePicker() {
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle(context.getString(R.string.language_picker_title))
//        builder.setItems(languages.toTypedArray(), { _, itemIdx ->
//            onLanguageSelectionListener?.onLanguageSelected(itemIdx)
//        })
//
//        // Create the dialog that overlays over the keyboard.
//        alert = builder.create() as CustomDialog
//        alert?.let {
//            val window = it.window
//            val lp = window.attributes
//            lp.token = windowToken
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
//            } else {
//                lp.type = WindowManager.LayoutParams.TYPE_PHONE
//            }
//
//            window.setFormat(PixelFormat.OPAQUE)
//            window.setDimAmount(0.5F)
//            window.addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
//                    WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//            it.show()
//        }

//        createLanguagePickerDialog().show()
        startLanguagePickerDialogActivity()
    }

    class CustomDialog(context: Context?) : Dialog(context) {
        override fun onTouchEvent(event: MotionEvent?): Boolean {
            if (isShowing) {
                if (event?.action == MotionEvent.ACTION_OUTSIDE) {
                    dismiss()
                    return true
                }
            }
            return super.onTouchEvent(event)
        }

        override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
            if(event?.keyCode != KeyEvent.KEYCODE_UNKNOWN) {
                dismiss();
            }
            return super.dispatchKeyEvent(event)
        }

        override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
            return super.dispatchTouchEvent(ev)
        }

        class Builder {

        }
    }
//
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//
//     if (event?.keyCode != KeyEvent.KEYCODE_UNKNOWN) {
//            alert?.dismiss()
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }

    override fun onDisplayHint(hint: Int) {
        super.onDisplayHint(hint)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event?.keyCode != KeyEvent.KEYCODE_UNKNOWN) {
            alert?.dismiss()
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    private fun drawSubKeys(canvas: Canvas) {
        if (keyboard != null) {
            for (i in 0 until keyboard.keys.size) {
                val key = keyboard.keys[i] as GwaKeyboardKey
                if (key.subLabel != null) {
                    val subKeyPaint = textPaint
                    subKeyPaint.color = resources.getColor(R.color.subKeyTextColor)
                    subKeyPaint.typeface = Typeface.DEFAULT
                    subKeyPaint.textSize = baseTextSize
                    val paddingX = key.width * resources.getFraction(R.fraction.sub_key_padding_x_fraction, 1, 1)
                    val paddingY = key.height * resources.getFraction(R.fraction.sub_key_padding_y_fraction, 1, 1)
                    val startX = key.width + key.x - paddingX
                    val startY = paddingY + key.y
                    canvas.drawText(key.subLabel as String?, startX, startY, subKeyPaint)
                }
            }
        }
    }

    private fun drawLanguageOnSpaceBar(canvas: Canvas) {
        keyboard?.let {
            for (i in 0 until keyboard.keys.size) {
                val key = keyboard.keys[i] as GwaKeyboardKey
                if (key.codes.isNotEmpty() && key.codes[0] == KEYCODE_SPACE) {
                    val languagePaint = textPaint
                    languagePaint.color = resources.getColor(R.color.languageTextColor)
                    languagePaint.typeface = Typeface.DEFAULT
                    languagePaint.textSize = baseTextSize * 1.5f
                    languagePaint.textAlign = Paint.Align.CENTER
                    val startX = key.x.toFloat() + key.width * 0.5f
                    val startY = key.y.toFloat() + key.height * 0.7f
                    canvas.drawText(
                            currentLanguage,
                            startX,
                            startY,
                            languagePaint)
                }
            }
        }
    }

    private fun loadKeyCodes() {
        KEYCODE_SPACE = resources.getInteger(R.integer.keycode_space)
    }

    companion object {
        var KEYCODE_NONE = -777
        var KEYCODE_SPACE = KEYCODE_NONE
        var count = 0
    }
}

interface OnLanguageSelectionListener {
    fun onLanguageSelected(languageIdx: Int)
}
