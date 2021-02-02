package com.test.runar.CustomClasses

import android.graphics.Typeface
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.TypefaceSpan
import org.xml.sax.XMLReader

class InterTagHandler : Html.TagHandler {
    private var startIndex = 0;
    private var endIndex = 0;
    private var font : Typeface? = null
    constructor(typeface: Typeface){
        this.font = typeface
    }


    override fun handleTag(opening: Boolean, tag: String?, output: Editable?, xmlReader: XMLReader?) {
        if (tag?.toLowerCase().equals("bf")) {
            if (output != null) {
                if (opening) {
                    startIndex = output.length
                } else {
                    endIndex = output.length
                    output.setSpan(CustomTypefaceSpan(font), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }
}