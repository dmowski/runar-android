package com.tnco.runar.repository

import android.util.Log
import com.tnco.runar.RunarLogger
import com.tnco.runar.data.local.AppDao
import com.tnco.runar.data.local.DataDao
import com.tnco.runar.di.annotations.EnLocale
import com.tnco.runar.di.annotations.RuLocale
import com.tnco.runar.domain.entities.LibraryItem
import com.tnco.runar.model.*
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val dataDao: DataDao,
    private val languageRepository: LanguageRepository,
    @RuLocale private val ruAppDao: Lazy<AppDao>,
    @EnLocale private val enAppDao: Lazy<AppDao>
) {

    private fun appDao(): AppDao {
        return if (languageRepository.currentAppLanguage() == "ru") ruAppDao.get() else enAppDao.get()
    }

    fun notShow(id: Int) {
        dataDao.notShow(id)
    }

    fun getLayoutDetails(id: Int): LayoutDescriptionModel {
        return appDao().getLayoutDetails(id)
    }

    fun getAllLayouts(): List<LayoutDescriptionModel> {
        return appDao().getAllLayoutDetails()
    }

    fun getShowStatus(id: Int): Int {
        return dataDao.getShowStatus(id)
    }

    fun getRunesList(): List<RuneDescriptionModel> {
        return appDao().getRunesDetails()
    }

    fun getAffirmList(): List<AffimDescriptionModel> {
        return appDao().getAffirmations()
    }

    fun getTwoRunesInterpretation(id: Int): String {
        return appDao().getTwoRunesInter(id)
    }

    fun getAllTwoRunesInter(): List<TwoRunesInterModel> {
        return appDao().getAllTwoRunesInter()
    }

    fun addUserLayout(data: UserLayoutModel) {
        dataDao.addUserLayout(data)
    }

    fun getLayoutName(id: Int): String {
        return appDao().getLayoutName(id)
    }

    fun getLibraryItemList(): List<LibraryItemsModel> {
        return appDao().getLibraryItems()
    }

    fun getLibraryRootItemsList(): List<LibraryItem> =
        appDao().getLibraryRootItemsList().map {
            LibraryItem.fromLibraryItemsModel(it)
        }
//        appDao().getLibraryRootItemsList().map { list -> //TODO extract mapping to fun
//            list.map {
//                LibraryItem.fromLibraryItemsModel(it)
//            }
//        }

    fun getFilteredLibraryItemsList(idList: List<String>): List<LibraryItem> {
        Log.d("99999", "getFilteredLibraryItemsList id $idList")
        val l = appDao().getFilteredLibraryItemsList2(idList = idList).map {
            LibraryItem.fromLibraryItemsModel(it)
        }
        val list = appDao().getFilteredLibraryItemsList(idList = idList).map { list ->
            list.map {
                LibraryItem.fromLibraryItemsModel(it)
            }
        }
        Log.d("99999", "getFilteredLibraryItemsList list $l")
        return l
    }

    fun updateLibraryDB(list: List<LibraryItemsModel>) {
        appDao().clearLibrary()
        RunarLogger.logDebug("library cleared")
        appDao().insertLibraryData(list)
        RunarLogger.logDebug("library data inserted")
    }

    fun getUserLayouts(): List<UserLayoutModel> {
        return dataDao.getUserLayouts()
    }

    fun deleteUserLayoutsByIds(ids: List<Int>) {
        dataDao.removeUserLayoutsByIds(ids)
    }

    fun getRunesGenerator(): Flow<List<RunesItemsModel>> {
        return appDao().getRunesGenerator()
    }

    fun updateRunesGeneratorDB(list: List<RunesItemsModel>) {
        appDao().clearRunesGenerator()
        appDao().insertRunesGenerator(list)
    }
}
