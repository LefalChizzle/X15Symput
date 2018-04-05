package com.amosgwa.lisukeyboard.extensions

import android.util.TypedValue
import android.view.View

fun View.toDp(px: Int): Int {
    return toDp(px.toFloat()).toInt()
}

fun View.toDp(px: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resources.displayMetrics)
}

