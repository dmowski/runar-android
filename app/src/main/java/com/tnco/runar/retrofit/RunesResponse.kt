package com.tnco.runar.retrofit


import com.google.gson.annotations.SerializedName

data class RunesResponse(
    val en: En? = null,
    val id: Int? = null,
    val imgUrl: String? = null,
    val ru: Ru? = null
)