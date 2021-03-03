package com.test.runar.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.test.runar.model.*

@Dao
interface AppDAO {

    @Query("SELECT * FROM layouts WHERE layout_id = :id")
    suspend fun getLayoutDetails(id: Int): LayoutDescriptionModel

    @Query("SELECT layout_name FROM layouts WHERE layout_id = :id")
    suspend fun getLayoutName(id: Int?): String

    @Query("SELECT * FROM runes")
    suspend fun getRunesDetails(): List<RuneDescriptionModel>

    @Query("SELECT * FROM affirmations")
    suspend fun getAffirmations(): List<AffimDescriptionModel>

    @Query("SELECT text FROM two_runes WHERE id = :id")
    suspend fun getTwoRunesInter(id: Int?) : String

    @Query("SELECT * FROM library")
    suspend fun getLibraryItems(): List<LibraryItemsModel>
}