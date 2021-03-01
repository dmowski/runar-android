package com.test.runar.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class LibraryItemsModel(
    var icon: Int?,
    var header: String?,
    var text: String?,
    var typeId: Int?,
    var menuId: Int?,
    var id: Int?,
    var subMenuId: Int?
)