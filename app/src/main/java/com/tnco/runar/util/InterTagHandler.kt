package com.tnco.runar.util

import android.graphics.Typeface
import android.text.Editable
import android.text.Html
import android.text.Spanned
import org.xml.sax.XMLReader

class InterTagHandler(typeface: Typeface) : Html.TagHandler {
    private var startIndex = 0
    private var endIndex = 0
    private var font: Typeface? = typeface

    override fun handleTag(
        opening: Boolean,
        tag: String?,
        output: Editable?,
        xmlReader: XMLReader?
    ) {
        if (tag?.lowercase().equals("bf")) {
            if (output != null) {
                if (opening) {
                    startIndex = output.length
                } else {
                    endIndex = output.length
                    output.setSpan(
                        CustomTypefaceSpan(font),
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }
    }
}
