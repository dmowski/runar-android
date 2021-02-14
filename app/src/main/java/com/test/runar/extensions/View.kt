package com.test.runar.extensions

import android.view.View

fun List<View>.setOnCLickListenerForAll(clickListener: View.OnClickListener) {
    this.forEach {
        it.setOnClickListener(clickListener)
    }
}