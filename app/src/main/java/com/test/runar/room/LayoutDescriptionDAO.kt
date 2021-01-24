package com.test.runar.room

import androidx.room.Dao
import androidx.room.Query
import com.test.runar.model.LayoutDescriptionModel

@Dao
interface LayoutDescriptionDAO {

    @Query("UPDATE layouts SET show = 0 WHERE layout_id = :id")
    suspend fun notShow(id: Int?)

    @Query("SELECT * FROM layouts WHERE layout_id = :id")
    suspend fun getLayoutDetails(id: Int?): LayoutDescriptionModel

    @Query("SELECT show FROM layouts WHERE layout_id = :id")
    suspend fun getShowStatus(id: Int?): Int
}