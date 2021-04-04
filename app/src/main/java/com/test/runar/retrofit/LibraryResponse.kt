package com.test.runar.retrofit

data class LibraryResponse(
    val childIds : List<String>? = null,
    val _id: String? = null,
    val imageUrl: String? = null,
    val title: String? =null,
    val sortOrder: Int? = null,
    val content: String? =null,
    val type: String? = null,
    val linkTitle: String? =null,
    val linkUrl: String? = null
)
