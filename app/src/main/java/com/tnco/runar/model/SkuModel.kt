package com.tnco.runar.model

data class SkuModel(
    val id: Int,
    val title: String,
    val description: String,
    val cost: String,
    val currencySign: String,
    val discount: String? = null
)
