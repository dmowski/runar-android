package com.test.runar.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.runar.model.LayoutDescriptionModel

@Database(entities = [LayoutDescriptionModel::class], version = 1)
abstract class LayoutDescriptionDB : RoomDatabase() {
    abstract fun layoutDescriptionDAO() : LayoutDescriptionDAO

    companion object{
        @Volatile
        private var INSTANCE: LayoutDescriptionDB? = null

        fun getLayoutDB(context: Context): LayoutDescriptionDB{
            if(INSTANCE!=null) return INSTANCE!!
            synchronized(this){
                INSTANCE = Room.databaseBuilder(context,LayoutDescriptionDB::class.java,"LD_DATABASE").createFromAsset("database/layouts.db").allowMainThreadQueries().build()
                return INSTANCE!!
            }
        }
    }
}