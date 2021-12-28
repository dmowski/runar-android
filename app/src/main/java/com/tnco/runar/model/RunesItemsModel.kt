package com.tnco.runar.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "runes_generator")
data class RunesItemsModel(
    @PrimaryKey
    var id: Int,
    var imgUrl: String?,
    @Ignore
    var enDesc: String?,
    var enTitle: String?,
    @Ignore
    var ruDesc: String?,
    var ruTitle: String?
){
    constructor(): this(0,null,null,null,null,null)
}
