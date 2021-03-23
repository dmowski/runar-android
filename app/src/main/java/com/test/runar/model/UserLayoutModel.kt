package com.test.runar.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_layouts")
data class UserLayoutModel(
    @ColumnInfo(name = "user_id")
    var userId: String?,
    @ColumnInfo(name = "save_date")
    var saveDate: Long?,
    @ColumnInfo(name = "layout_id")
    var layoutId: Int?,
    var slot1: Int?,
    var slot2: Int?,
    var slot3: Int?,
    var slot4: Int?,
    var slot5: Int?,
    var slot6: Int?,
    var slot7: Int?,
    var interpretation: String?,
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}