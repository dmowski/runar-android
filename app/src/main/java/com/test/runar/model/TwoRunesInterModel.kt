package com.test.runar.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "two_runes")
data class TwoRunesInterModel(

    @PrimaryKey
    var id: Int?,
    var text: String?
)