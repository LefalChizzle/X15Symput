package com.symput.mathkeyboard.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.symput.mathkeyboard.R
import com.symput.mathkeyboard.databinding.DialogViewBinding

class CustomDialog : DialogFragment() {

    private var listener: Listener? = null
    private var title: String? = null
    private var message: String? = null
    private var positiveText: String? = null
    private var negativeText: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<DialogViewBinding>(
                inflater,
                R.layout.dialog_view,
                container,
                false
        ).apply {
            message.text = this@CustomDialog.message
            title.text = this@CustomDialog.title
            positiveButton.isVisible(!positiveText.isNullOrEmpty())
            negativeButton.isVisible(!negativeText.isNullOrEmpty())
            positiveButton.text = positiveText
            negativeButton.text = negativeText
            positiveButton.setOnClickListener { listener?.onPositiveSelect(this@CustomDialog) }
            negativeButton.setOnClickListener { listener?.onNegativeSelect(this@CustomDialog) }
        }.root
    }

    class Builder {
        private val dialog = CustomDialog()

        fun title(t: String): Builder {
            dialog.title = t
            return this
        }

        fun message(msg: String): Builder {
            dialog.message = msg
            return this
        }

        fun listener(l: Listener): Builder {
            dialog.listener = l
            return this
        }

        fun positiveText(t: String): Builder {
            dialog.positiveText = t
            return this
        }

        fun negativeText(t: String): Builder {
            dialog.negativeText = t
            return this
        }

        fun build(): CustomDialog {
            return dialog
        }
    }

    private fun View.isVisible(v: Boolean) {
        visibility = if (v) View.VISIBLE else View.GONE
    }

    interface Listener {
        fun onPositiveSelect(dialog: CustomDialog)
        fun onNegativeSelect(dialog: CustomDialog)
    }

    companion object {
        const val TAG = "custom_dialog_tag"
    }
}