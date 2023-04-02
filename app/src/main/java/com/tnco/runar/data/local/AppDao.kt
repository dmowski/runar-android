package com.tnco.runar.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tnco.runar.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Query("SELECT * FROM layouts WHERE layout_id = :id")
    fun getLayoutDetails(id: Int): LayoutDescriptionModel

    @Query("SELECT * FROM layouts")
    fun getAllLayoutDetails(): List<LayoutDescriptionModel>

    @Query("SELECT layout_name FROM layouts WHERE layout_id = :id")
    fun getLayoutName(id: Int?): String

    @Query("SELECT * FROM runes")
    fun getRunesDetails(): List<RuneDescriptionModel>

    @Query("SELECT * FROM affirmations")
    fun getAffirmations(): List<AffimDescriptionModel>

    @Query("SELECT text FROM two_runes WHERE id = :id")
    fun getTwoRunesInter(id: Int?): String

    @Query("SELECT * FROM two_runes")
    fun getAllTwoRunesInter(): List<TwoRunesInterModel>

    @Query("SELECT * FROM library")
    fun getLibraryItems(): List<LibraryItemsModel>

    @Query("SELECT * FROM library WHERE type = \"root\" ORDER BY sort_order ASC")
    fun getLibraryRootItemsList(): List<LibraryItemsModel>

    @Query("SELECT * FROM library WHERE id IN (:idList) ORDER BY sort_order ASC")
    fun getFilteredLibraryItemsList(idList: List<String>): List<LibraryItemsModel>

    @Query("DELETE FROM library")
    fun clearLibrary()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLibraryData(data: List<LibraryItemsModel>)

    @Insert
    fun insertRunesGenerator(data: List<RunesItemsModel>)

    @Query("SELECT * FROM runes_generator")
    fun getRunesGenerator(): Flow<List<RunesItemsModel>>

    @Query("DELETE FROM runes_generator")
    fun clearRunesGenerator()
}
