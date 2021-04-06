package com.tnco.runar.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tnco.runar.model.*

@Dao
interface AppDAO {

    @Query("SELECT * FROM layouts WHERE layout_id = :id")
    suspend fun getLayoutDetails(id: Int): LayoutDescriptionModel

    @Query("SELECT * FROM layouts")
    fun getAllLayoutDetails(): List<LayoutDescriptionModel>

    @Query("SELECT layout_name FROM layouts WHERE layout_id = :id")
    suspend fun getLayoutName(id: Int?): String

    @Query("SELECT * FROM runes")
    fun getRunesDetails(): List<RuneDescriptionModel>

    @Query("SELECT * FROM affirmations")
    suspend fun getAffirmations(): List<AffimDescriptionModel>

    @Query("SELECT text FROM two_runes WHERE id = :id")
    suspend fun getTwoRunesInter(id: Int?) : String

    @Query("SELECT * FROM two_runes")
    fun getAllTwoRunesInter() : List<TwoRunesInterModel>

    @Query("SELECT * FROM library")
    fun getLibraryItems(): List<LibraryItemsModel>

    @Query("DELETE FROM library")
    suspend fun clearLibrary()

    @Insert
    suspend fun insertLibraryData(data: List<LibraryItemsModel>)

}