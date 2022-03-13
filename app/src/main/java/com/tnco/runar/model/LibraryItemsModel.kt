package com.tnco.runar.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tnco.runar.room.LibraryConverter

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
    @ColumnInfo(name = "link_title")
    var linkTitle: String?,
    @ColumnInfo(name = "link_url")
    var linkUrl: String?,
    @ColumnInfo(name = "audio_url")
    var audioUrl: String?,
    @ColumnInfo(name = "audio_duration")
    var audioDuration: Int?
) {
    constructor() : this("null", listOf(), null, null, null, null, null, null, null,null,null)

    override fun toString(): String {
        return "LibraryItemsModel(title=$title, audioUrl=$audioUrl, audioDuration=$audioDuration)"
    }


}
