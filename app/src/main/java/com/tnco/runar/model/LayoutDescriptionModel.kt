package com.tnco.runar.model

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
    var slot1: Int?,
    var slot2: Int?,
    var slot3: Int?,
    var slot4: Int?,
    var slot5: Int?,
    var slot6: Int?,
    var slot7: Int?,
    @ColumnInfo(name = "slot1_meaning")
    var slotMeaning1 : String?,
    @ColumnInfo(name = "slot2_meaning")
    var slotMeaning2 : String?,
    @ColumnInfo(name = "slot3_meaning")
    var slotMeaning3 : String?,
    @ColumnInfo(name = "slot4_meaning")
    var slotMeaning4 : String?,
    @ColumnInfo(name = "slot5_meaning")
    var slotMeaning5 : String?,
    @ColumnInfo(name = "slot6_meaning")
    var slotMeaning6 : String?,
    @ColumnInfo(name = "slot7_meaning")
    var slotMeaning7 : String?,
    @ColumnInfo(name = "interpretation")
    var interpretation : String?
)