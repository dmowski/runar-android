package com.tnco.runar.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tnco.runar.model.UserLayoutModel

@Dao
interface DataDao {

    @Query("UPDATE description_status SET status = 0 WHERE layout_id = :id")
    fun notShow(id: Int)

    @Query("SELECT status FROM description_status WHERE layout_id = :id")
    fun getShowStatus(id: Int): Int

    @Insert
    fun addUserLayout(data: UserLayoutModel)

    @Query("SELECT * FROM user_layouts")
    fun getUserLayouts(): List<UserLayoutModel>

    @Query("DELETE from user_layouts where id in (:idList)")
    fun removeUserLayoutsByIds(idList: List<Int>)
}
