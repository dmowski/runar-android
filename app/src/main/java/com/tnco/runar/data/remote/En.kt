package com.tnco.runar.data.remote

import com.google.gson.annotations.SerializedName

data class En(
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("title")
    val title: String? = null
)
