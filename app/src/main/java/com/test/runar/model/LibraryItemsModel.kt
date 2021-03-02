package com.test.runar.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class LibraryItemsModel(
    var icon: Int?,
    var title: String?,
    var text: String?,
    var typeView: String?,
    var id: Int?,
    var parentId: Int?
)