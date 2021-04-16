package com.symput.mathkeyboard.utilities

import android.util.Log
import com.symput.mathkeyboard.extensions.biLet

class MsTimer {
    private var startTime: Long? = null
    private var endTime: Long? = null
    private var durationMs: Int = 0

    fun start() {
        startTime = System.nanoTime()
    }

    fun end() {
        endTime = System.nanoTime()
    }

    private fun duration() {
        biLet(endTime, startTime) { endTime, startTime ->
            durationMs = ((endTime - startTime) / 1000000).toInt()
        }
    }

    fun print(tag: String) {
        duration()
        Log.d(tag, durationMs.toString())
    }
}