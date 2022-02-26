package com.tnco.runar.retrofit

import android.graphics.Bitmap

data class BackgroundInfo (val name: String, val imgUrl: String, var img: Bitmap?,var isSelected: Boolean = false){
}