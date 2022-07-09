package com.tnco.runar.repository

import com.tnco.runar.RunarLogger
import com.tnco.runar.model.*
import com.tnco.runar.room.AppDB
import com.tnco.runar.room.DataDB
import kotlinx.coroutines.flow.Flow

object DatabaseRepository {
    private var appDao = AppDB.getLayoutDB().appDAO()
    private var dataDao = DataDB.getDataDB().dataDAO()
    fun reinit(){
        appDao = AppDB.getLayoutDB().appDAO()
        dataDao = DataDB.getDataDB().dataDAO()
    }

    fun notShow(id: Int) {
        dataDao.notShow(id)
    }

    fun getLayoutDetails(id: Int): LayoutDescriptionModel {
        return appDao.getLayoutDetails(id)
    }

    fun getAllLayouts(): List<LayoutDescriptionModel>{
        return appDao.getAllLayoutDetails()
    }

    fun getShowStatus(id: Int): Int {
        return dataDao.getShowStatus(id)
    }

    fun getRunesList(): List<RuneDescriptionModel> {
        return appDao.getRunesDetails()
    }

    fun getAffirmList(): List<AffimDescriptionModel> {
        return appDao.getAffirmations()
    }

    fun getTwoRunesInterpretation(id: Int): String {
        return appDao.getTwoRunesInter(id)
    }

    fun getAllTwoRunesInter(): List<TwoRunesInterModel>{
        return appDao.getAllTwoRunesInter()
    }

    fun addUserLayout(data: UserLayoutModel) {
        dataDao.addUserLayout(data)
    }

    fun getLayoutName(id: Int): String{
        return appDao.getLayoutName(id)
    }

    fun getLibraryItemList(): List<LibraryItemsModel> {
        return appDao.getLibraryItems()
    }

    fun updateLibraryDB(list: List<LibraryItemsModel>){
        appDao.clearLibrary()
        RunarLogger.logDebug("library cleared")
        appDao.insertLibraryData(list)
        RunarLogger.logDebug("library data inserted")
    }

    fun getUserLayouts(): List<UserLayoutModel>{
        return dataDao.getUserLayouts()
    }

    fun deleteUserLayoutsByIds(ids: List<Int>){
        dataDao.removeUserLayoutsByIds(ids)
    }


    fun getRunesGenerator(): Flow<List<RunesItemsModel>> {
        return appDao.getRunesGenerator()
    }

    fun updateRunesGeneratorDB(list: List<RunesItemsModel>){
        appDao.clearRunesGenerator()
        appDao.insertRunesGenerator(list)
    }
}