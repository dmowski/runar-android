package com.test.runar.repository

import com.test.runar.RunarLogger
import com.test.runar.model.*
import com.test.runar.room.AppDB
import com.test.runar.room.DataDB

object DatabaseRepository {
    private var appDao = AppDB.getLayoutDB().appDAO()
    private var dataDao = DataDB.getDataDB().dataDAO()
    fun reinit(){
        appDao = AppDB.getLayoutDB().appDAO()
        dataDao = DataDB.getDataDB().dataDAO()
    }

    suspend fun notShow(id: Int) {
        dataDao.notShow(id)
    }

    suspend fun getLayoutDetails(id: Int): LayoutDescriptionModel {
        return appDao.getLayoutDetails(id)
    }

    suspend fun getShowStatus(id: Int): Int {
        return dataDao.getShowStatus(id)
    }

    suspend fun getRunesList(): List<RuneDescriptionModel> {
        return appDao.getRunesDetails()
    }

    suspend fun getAffirmList(): List<AffimDescriptionModel> {
        return appDao.getAffirmations()
    }

    suspend fun getTwoRunesInterpretation(id: Int): String {
        return appDao.getTwoRunesInter(id)
    }

    suspend fun addUserLayout(data: UserLayoutModel) {
        dataDao.addUserLayout(data)
    }

    suspend fun getLayoutName(id: Int): String{
        return appDao.getLayoutName(id)
    }

    suspend fun getLibraryItemList(): List<LibraryItemsModel> {
        return appDao.getLibraryItems()
    }

    suspend fun updateLibraryDB(list: List<LibraryItemsModel>){
        appDao.clearLibrary()
        RunarLogger.logDebug("library cleared")
        appDao.insertLibraryData(list)
        RunarLogger.logDebug("library data inserted")
    }

    suspend fun getUserLayouts(): List<UserLayoutModel>{
        return dataDao.getUserLayouts()
    }

    suspend fun deleteUserLayoutsByIds(ids: List<Int>){
        dataDao.removeUserLayoutsByIds(ids)
    }
}