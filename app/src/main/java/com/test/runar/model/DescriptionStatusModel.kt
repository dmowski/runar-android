package com.test.runar.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "description_status")
data class DescriptionStatusModel(

    @PrimaryKey
    var layout_id: Int?,
    var status: Boolean?
)