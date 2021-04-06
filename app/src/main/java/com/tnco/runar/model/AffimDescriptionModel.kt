package com.tnco.runar.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "affirmations")
data class AffimDescriptionModel(

    @PrimaryKey
    var id: Int?,
    var lvl1: String?,
    var lvl2: String?,
    var lvl3: String?,
    var lvl4: String?,
)