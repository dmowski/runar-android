package com.tnco.runar.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tnco.runar.model.*
import java.util.*

@Database(
    entities = [LayoutDescriptionModel::class, RuneDescriptionModel::class, AffimDescriptionModel::class, TwoRunesInterModel::class, LibraryItemsModel::class],
    version = 2,
    exportSchema = false
)

abstract class AppDB : RoomDatabase() {
    abstract fun appDAO(): AppDAO

    companion object {
        @Volatile
        private lateinit var INSTANCE: AppDB

        fun init(context: Context) {
            val locale: String = Locale.getDefault().language
            var dataBaseFilePath = ""
            var dataBaseName =""

            if (locale.equals("ru")) {
                dataBaseFilePath = "database/layouts.db"
                dataBaseName = "RU_DATABASE"
            } else {
                dataBaseFilePath = "database/en_layouts.db"
                dataBaseName = "EN_DATABASE"
            }

            INSTANCE = Room.databaseBuilder(context, AppDB::class.java, dataBaseName)
                    .createFromAsset(dataBaseFilePath).build()
        }

        fun getLayoutDB(): AppDB {
            return INSTANCE
        }
    }
}