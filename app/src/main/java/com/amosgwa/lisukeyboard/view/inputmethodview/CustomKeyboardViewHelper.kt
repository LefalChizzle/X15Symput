package com.amosgwa.lisukeyboard.view.inputmethodview

//
//// Create views for each individual keys for a keyboard
//private fun getKeyViews(rows: List<List<CustomKey>>, keyboardLanguage: String): MutableList<MutableList<CustomKeyView>> {
//    val keyViews = mutableListOf<MutableList<CustomKeyView>>()
//    rows.forEach { row ->
//        // Keep track of the row keys.
//        val rowKeyViews = mutableListOf<CustomKeyView>()
//        // Create row linear layout for the key views.
//        row.forEach { key ->
//            // The background of the key has to be duplicate since the keys have different widths.
//            val keyBackgroundCopy = keyBackground?.constantState?.newDrawable()?.mutate()
//            val keyView = CustomKeyView(
//                    context,
//                    key = key,
//                    globalTextColor = globalKeyTextColor,
//                    globalTextSize = globalKeyTextSize,
//                    isLandscape = isLandscape,
//                    globalKeyBackground = keyBackgroundCopy
//            )
//            // Update the language for the key that is assigned with isChange
//            if (key.isChangeLanguageKey) {
//                keyView.updateLabel(keyboardLanguage)
//            }
//            // Keeps track of all of the key views
//            rowKeyViews.add(keyView)
//        }
//
//        // Key tracks of the rows with key views.
//        keyViews.add(rowKeyViews.toMutableList())
//        rowKeyViews.clear()
//    }
//    return keyViews
//}
