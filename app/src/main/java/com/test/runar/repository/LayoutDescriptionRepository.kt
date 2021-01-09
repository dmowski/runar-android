package com.test.runar.repository

import android.content.Context
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.room.LayoutDescriptionDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class LayoutDescriptionRepository {
    companion object{

        var layoutDescriptionDB: LayoutDescriptionDB? = null

        private fun initDB(context: Context): LayoutDescriptionDB{
            return LayoutDescriptionDB.getLayoutDB(context)
        }

        fun notShow(context: Context, id: Int){
            layoutDescriptionDB = initDB(context)
            CoroutineScope(IO).launch {
                layoutDescriptionDB!!.layoutDescriptionDAO().notShow(id)
            }
        }

        fun getLayoutDetails(context: Context,id: Int) : LayoutDescriptionModel{
            layoutDescriptionDB = initDB(context)
            return layoutDescriptionDB!!.layoutDescriptionDAO().getLayoutDetails(id)
        }

        fun getShowStatus(context: Context,id:Int):Boolean{
            layoutDescriptionDB = initDB(context)
            return layoutDescriptionDB!!.layoutDescriptionDAO().getShowStatus(id) != 0
        }
    }
}