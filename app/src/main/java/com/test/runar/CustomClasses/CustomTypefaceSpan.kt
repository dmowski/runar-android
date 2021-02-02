package com.test.runar.CustomClasses

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import android.text.style.TypefaceSpan


class CustomTypefaceSpan(val font: Typeface?) : MetricAffectingSpan() {
    override fun updateMeasureState(textPaint: TextPaint) = update(textPaint)
    override fun updateDrawState(textPaint: TextPaint?) = update(textPaint)

    private fun update(tp: TextPaint?) {
        tp.apply {
            val old = this!!.typeface
            val oldStyle = old?.style ?: 0
            val font = Typeface.create(font, oldStyle)
            typeface = font
        }
    }
}