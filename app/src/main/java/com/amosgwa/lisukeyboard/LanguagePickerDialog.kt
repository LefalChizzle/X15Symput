package com.amosgwa.lisukeyboard

import android.content.DialogInterface
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.widget.EditText


/**
 * Created by Amos Gwa on 3/16/2018.
 */
class LanguagePickerDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Pick a language")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    // FIRE ZE MISSILES!
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                })
        // Create the AlertDialog object and return it
        return builder.create()
    }

    companion object {
        var selectedText = ""

        fun createDialog(context: Context): AlertDialog {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Pick a language")
            // Set up the input
            val input = EditText(context)
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            builder.setView(input)

            // Set up the buttons
            builder.setPositiveButton("OK") { dialog, which -> selectedText = input.text.toString() }
            builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
            return builder.create();
        }
    }
}