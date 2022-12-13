package com.tnco.runar.data.remote

import com.google.gson.annotations.SerializedName

data class RunesResponse(
    @SerializedName("en")
    val en: En? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("imgUrl")
    val imgUrl: String? = null,
    @SerializedName("ru")
    val ru: Ru? = null
)
