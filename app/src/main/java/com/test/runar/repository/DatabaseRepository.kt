package com.test.runar.repository

import com.test.runar.model.AffimDescriptionModel
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.model.RuneDescriptionModel
import com.test.runar.model.UserLayoutModel
import com.test.runar.room.AppDB

object DatabaseRepository {
    private val appDB: AppDB = AppDB.getLayoutDB()

    suspend fun notShow(id: Int) {
        appDB.appDAO().notShow(id)
    }

    suspend fun getLayoutDetails(id: Int): LayoutDescriptionModel {
        return appDB.appDAO().getLayoutDetails(id)
    }

    suspend fun getShowStatus(id: Int): Int {
        return appDB.appDAO().getShowStatus(id)
    }

    suspend fun getRunesList(): List<RuneDescriptionModel> {
        return appDB.appDAO().getRunesDetails()
    }

    suspend fun getAffirmList(): List<AffimDescriptionModel> {
        return appDB.appDAO().getAffirmations()
    }

    suspend fun getTwoRunesInterpretation(id: Int): String {
        return appDB.appDAO().getTwoRunesInter(id)
    }

    suspend fun addUserLayout(data: UserLayoutModel) {
        appDB.appDAO().addUserLayout(data)
    }

    suspend fun closeDB() {
        if (appDB.isOpen) {
            appDB.close()
        }
    }

}