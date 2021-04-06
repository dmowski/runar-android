package com.tnco.runar.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runes")
data class RuneDescriptionModel(

    @PrimaryKey
    @ColumnInfo(name = "rune_id")
    var runeId: Int?,
    @ColumnInfo(name = "name")
    var runeName: String?,
    @ColumnInfo(name = "description_full")
    var fullDescription: String?,
    @ColumnInfo(name = "description")
    var shortDescription: String?,
    var meaning: String?,
    var ausp: Int?,
)