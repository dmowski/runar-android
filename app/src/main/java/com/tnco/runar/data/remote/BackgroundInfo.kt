package com.tnco.runar.retrofit

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class BackgroundInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("imgUrl")
    val imgUrl: String,
    var img: Bitmap?,
    var isSelected: Boolean = false
)
