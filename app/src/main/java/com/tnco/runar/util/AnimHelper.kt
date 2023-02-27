package com.tnco.runar.util

import android.graphics.drawable.AnimatedVectorDrawable
import android.widget.ImageView
import androidx.core.view.isVisible

fun startAnim(view: ImageView) {
    view.isVisible = true
    val drawable: AnimatedVectorDrawable = view.drawable as AnimatedVectorDrawable
    drawable.start()
}