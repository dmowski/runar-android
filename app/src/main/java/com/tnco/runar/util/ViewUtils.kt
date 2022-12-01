package com.tnco.runar.util

import android.view.View

fun List<View>.setOnCLickListenerForAll(clickListener: View.OnClickListener) {
    this.forEach {
        it.setOnClickListener(clickListener)
    }
}
