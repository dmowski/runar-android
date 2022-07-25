package com.tnco.runar.model

import androidx.room.*
import com.tnco.runar.data.local.LibraryConverter

@Entity(tableName = "library")
@TypeConverters(LibraryConverter::class)
data class LibraryItemsModel constructor(
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
    @Ignore
    constructor() : this("null", listOf(), null, null, null, null, null, null, null, null, null)

    override fun toString(): String {
        return "LibraryItemsModel(title=$title, audioUrl=$audioUrl, audioDuration=$audioDuration)"
    }


}
