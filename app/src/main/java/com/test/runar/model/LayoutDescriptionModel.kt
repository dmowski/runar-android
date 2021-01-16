package com.test.runar.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "layouts")
data class LayoutDescriptionModel(

    @PrimaryKey
    @ColumnInfo(name = "layout_id")
    var layoutId: Int?,
    @ColumnInfo(name = "layout_name")
    var layoutName: String?,
    @ColumnInfo(name = "layout_description")
    var layoutDescription: String?,
    @ColumnInfo(name = "max_lines")
    var maxLines: Int?,
    var show: Boolean?
)