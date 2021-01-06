package com.test.runar.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.test.runar.model.LayoutDescriptionModel

@Dao
interface LayoutDescriptionDAO {
    @Query("UPDATE layouts SET show = 0 WHERE layout_id = :id")
    suspend fun notShow(id: Int?)

    @Query("SELECT * FROM layouts WHERE layout_id = :id")
    fun getLayoutDetails(id: Int?): LayoutDescriptionModel
    @Query("SELECT show FROM layouts WHERE layout_id = :id")
    fun getShowStatus(id: Int?): Int

}