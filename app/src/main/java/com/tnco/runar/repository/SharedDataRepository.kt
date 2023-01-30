package com.tnco.runar.repository

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.res.ResourcesCompat
import com.tnco.runar.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedDataRepository @Inject constructor(
    @ApplicationContext context: Context,
) {
    var fontSize: Float = 0.0f
        private set

    init {
        defineFontSize(context)
    }

    fun defineFontSize(context: Context) {
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
        fontSize = textSize - 2f
    }
}
