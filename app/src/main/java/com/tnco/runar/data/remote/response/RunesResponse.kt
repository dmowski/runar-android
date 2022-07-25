package com.tnco.runar.data.remote.response

data class RunesResponse(
    val id: Int? = null,
    val imgUrl: String? = null,
    val en: RuneInfo? = null,
    val ru: RuneInfo? = null
) {
    data class RuneInfo(
        val description: String? = null,
        val title: String? = null
    )
}