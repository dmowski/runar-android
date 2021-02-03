package com.test.runar.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.test.runar.model.AffimDescriptionModel
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.model.RuneDescriptionModel
import com.test.runar.model.UserLayoutModel

@Dao
interface AppDAO {

    @Query("UPDATE layouts SET show = 0 WHERE layout_id = :id")
    suspend fun notShow(id: Int?)

    @Query("SELECT * FROM layouts WHERE layout_id = :id")
    suspend fun getLayoutDetails(id: Int?): LayoutDescriptionModel

    @Query("SELECT show FROM layouts WHERE layout_id = :id")
    suspend fun getShowStatus(id: Int?): Int

    @Query("SELECT * FROM runes")
    suspend fun getRunesDetails(): List<RuneDescriptionModel>

    @Query("SELECT * FROM affirmations")
    suspend fun getAffirmations(): List<AffimDescriptionModel>

    @Query("SELECT text FROM two_runes WHERE id = :id")
    suspend fun getTwoRunesInter(id: Int?) : String

    @Insert
    suspend fun addUserLayout(data: UserLayoutModel)
}