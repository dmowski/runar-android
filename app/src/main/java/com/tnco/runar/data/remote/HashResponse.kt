package com.tnco.runar.data.remote

import com.google.gson.annotations.SerializedName

data class HashResponse(
    @SerializedName("hash")
    val hash: String? = null
)
