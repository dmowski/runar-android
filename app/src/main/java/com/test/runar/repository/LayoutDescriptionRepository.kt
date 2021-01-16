package com.test.runar.repository

import android.content.Context
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.room.LayoutDescriptionDB

class LayoutDescriptionRepository {
    companion object {

        var layoutDescriptionDB: LayoutDescriptionDB? = null

        private fun initDB(context: Context): LayoutDescriptionDB {
            return LayoutDescriptionDB.getLayoutDB(context)
        }

        suspend fun notShow(context: Context, id: Int) {
            layoutDescriptionDB = initDB(context)
            layoutDescriptionDB!!.layoutDescriptionDAO().notShow(id)
        }

        suspend fun getLayoutDetails(context: Context, id: Int): LayoutDescriptionModel {
            layoutDescriptionDB = initDB(context)
            return layoutDescriptionDB!!.layoutDescriptionDAO().getLayoutDetails(id)
        }

        suspend fun getShowStatus(context: Context, id: Int): Int {
            layoutDescriptionDB = initDB(context)
            return layoutDescriptionDB!!.layoutDescriptionDAO().getShowStatus(id)
        }
    }
}