package com.tnco.runar.data.remote

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("status")
    val status: String? = null
)
