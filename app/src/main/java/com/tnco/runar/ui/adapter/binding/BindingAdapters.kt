package com.tnco.runar.ui.adapter.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.tnco.runar.R

class BindingAdapters {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {

            if (imageUrl != null) {
                imageView.load(imageUrl) {
                    crossfade(100)
                    error(R.drawable.rune39)
                }
            }
        }
    }
}
