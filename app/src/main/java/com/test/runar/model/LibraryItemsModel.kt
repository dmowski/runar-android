package com.test.runar.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library")
data class LibraryItemsModel(
    var icon: String?,
    var title: String?,
    var text: String?,
    @ColumnInfo(name = "view_type")
    var typeView: String?,
    @PrimaryKey
    var id: Int?,
    @ColumnInfo(name = "parent_id")
    var parentId: Int?
)