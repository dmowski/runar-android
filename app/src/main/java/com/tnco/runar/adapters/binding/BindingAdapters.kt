package com.tnco.runar.adapters.binding

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tnco.runar.R

class BindingAdapters {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
            if (imageUrl != null) {
//                Log.i("Image", imageUrl)
               // val imageUrl2 = "http://runar-generator-api.herokuapp.com/img/runes/5.png"
                imageView.load(imageUrl) {
                    crossfade(100)
                    error(R.drawable.rune39)
                }

//  val  imageUrl2 = "http://runar-generator-api.herokuapp.com/img/runes/38.png"
//                Picasso.get()
//                    .load(imageUrl2)
//                    .fit()
//                    .placeholder(R.drawable.rune39)
//                    .into(imageView)
            }
        }
    }

}