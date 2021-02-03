package com.test.runar.repository

import android.content.Context
import com.test.runar.model.AffimDescriptionModel
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.model.RuneDescriptionModel
import com.test.runar.room.AppDB

class DatabaseRepository {
    companion object {

        var appDB: AppDB? = null

        private fun initDB(context: Context): AppDB {
            return AppDB.getLayoutDB(context)
        }

        suspend fun notShow(context: Context, id: Int) {
            appDB = initDB(context)
            appDB!!.appDAO().notShow(id)
        }

        suspend fun getLayoutDetails(context: Context, id: Int): LayoutDescriptionModel {
            appDB = initDB(context)
            return appDB!!.appDAO().getLayoutDetails(id)
        }

        suspend fun getShowStatus(context: Context, id: Int): Int {
            appDB = initDB(context)
            return appDB!!.appDAO().getShowStatus(id)
        }

        suspend fun getRunesList(context: Context): List<RuneDescriptionModel>{
            appDB = initDB(context)
            return appDB!!.appDAO().getRunesDetails()
        }

        suspend fun getAffirmList(context: Context): List<AffimDescriptionModel>{
            appDB = initDB(context)
            return appDB!!.appDAO().getAffirmations()
        }

        suspend fun getTwoRunesInterpretation(context: Context,id: Int): String{
            appDB = initDB(context)
            return appDB!!.appDAO().getTwoRunesInter(id)
        }
    }
}