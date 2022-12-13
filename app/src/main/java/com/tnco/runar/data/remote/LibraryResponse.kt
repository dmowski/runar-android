package com.tnco.runar.data.remote

import com.google.gson.annotations.SerializedName

data class LibraryResponse(
    @SerializedName("childIds")
    val childIds: List<String>? = null,
    @SerializedName("_id")
    val _id: String? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("sortOrder")
    val sortOrder: Int? = null,
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("linkTitle")
    val linkTitle: String? = null,
    @SerializedName("linkUrl")
    val linkUrl: String? = null,
    @SerializedName("audioUrl")
    val audioUrl: String? = null,
    @SerializedName("audioDuration")
    val audioDuration: Int? = null,
    @SerializedName("tags")
    val tags: List<String>? = null
)
