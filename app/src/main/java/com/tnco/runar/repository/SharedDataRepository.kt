package com.tnco.runar.repository

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.res.ResourcesCompat
import com.tnco.runar.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedDataRepository @Inject constructor() {
    var fontSize: Float = 0.0f
        private set

    fun init(context: Context) {
        fontSize = correctFontSize(context)
    }

    private fun correctFontSize(context: Context): Float {
        val text = context.resources.getString(R.string.text_calculation_helper)
        val paint = Paint()
        val bounds = Rect()
        val maxWidth = Resources.getSystem().displayMetrics.widthPixels * 0.84
        paint.typeface = ResourcesCompat.getFont(context, R.font.roboto_light)
        var textSize = 1f
        paint.textSize = 1f
        paint.getTextBounds(text, 0, text.length, bounds)
        var currentWidth = bounds.width()
        while (currentWidth < maxWidth) {
            textSize++
            paint.textSize = textSize
            paint.getTextBounds(text, 0, text.length, bounds)
            currentWidth = bounds.width()
        }
        return textSize - 2f
    }
}