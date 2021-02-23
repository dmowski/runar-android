package com.test.runar.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.test.runar.model.AffimDescriptionModel
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.model.RuneDescriptionModel
import com.test.runar.model.UserLayoutModel

@Dao
interface DataDAO {

   /* @Query("UPDATE layouts SET show = 0 WHERE layout_id = :id")
    suspend fun notShow(id: Int)*/

   /* @Query("SELECT show FROM layouts WHERE layout_id = :id")
    suspend fun getShowStatus(id: Int): Int*/

    //@Insert
    //suspend fun addUserLayout(data: UserLayoutModel)
}