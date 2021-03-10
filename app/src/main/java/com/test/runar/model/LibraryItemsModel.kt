package com.test.runar.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.test.runar.room.LibraryConverter

@Entity(tableName = "library")
@TypeConverters(LibraryConverter::class)
data class LibraryItemsModel(
    @PrimaryKey
    var id: String,
    var childs: List<String>?,
    var content: String?,
    var title: String?,
    @ColumnInfo(name = "image_url")
    var imageUrl: String?,
    @ColumnInfo(name = "sort_order")
    var sortOrder: Int?,
    var type: String?,
){
    constructor() : this("null", listOf(),null,null,null,null,null)
}