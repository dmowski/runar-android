package com.test.runar.repository

import com.test.runar.model.AffimDescriptionModel
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.model.RuneDescriptionModel
import com.test.runar.model.UserLayoutModel
import com.test.runar.room.AppDB

object DatabaseRepository {
    private val appDao = AppDB.getLayoutDB().appDAO()

    suspend fun notShow(id: Int) {
        appDao.notShow(id)
    }

    suspend fun getLayoutDetails(id: Int): LayoutDescriptionModel {
        return appDao.getLayoutDetails(id)
    }

    suspend fun getShowStatus(id: Int): Int {
        return appDao.getShowStatus(id)
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
        appDao.addUserLayout(data)
    }
}