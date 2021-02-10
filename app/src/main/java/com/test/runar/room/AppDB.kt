package com.test.runar.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.runar.model.*

@Database(entities = [LayoutDescriptionModel::class,RuneDescriptionModel::class,AffimDescriptionModel::class,TwoRunesInterModel::class,UserLayoutModel::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {
    abstract fun appDAO(): AppDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDB? = null

        fun getLayoutDB(context: Context): AppDB {
            if (INSTANCE != null) return INSTANCE!!
            synchronized(this) {
                INSTANCE =
                    Room.databaseBuilder(context, AppDB::class.java, "LD_DATABASE")
                        .createFromAsset("database/layouts.db").build()
                return INSTANCE!!
            }
        }
    }
}