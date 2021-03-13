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

    @Query("UPDATE description_status SET status = 0 WHERE layout_id = :id")
    suspend fun notShow(id: Int)

    @Query("SELECT status FROM description_status WHERE layout_id = :id")
    suspend fun getShowStatus(id: Int): Int

    @Insert
    suspend fun addUserLayout(data: UserLayoutModel)

    @Query("SELECT * FROM user_layouts")
    suspend fun getUserLayouts(): List<UserLayoutModel>

    @Query("DELETE from user_layouts where id in (:idList)")
    suspend fun removeUserLayoutsByIds(idList: List<Int>)
}