package com.test.runar.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.runar.model.*
import java.util.*

@Database(
    entities = [LayoutDescriptionModel::class, RuneDescriptionModel::class, AffimDescriptionModel::class, TwoRunesInterModel::class, LibraryItemsModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDB : RoomDatabase() {
    abstract fun appDAO(): AppDAO

    companion object {
        @Volatile
        private lateinit var INSTANCE: AppDB

        fun init(context: Context) {
            var locale: String = Locale.getDefault().language
            var dataBaseFilePath = ""

            if (locale.equals("ru")) {
                dataBaseFilePath = "database/layouts.db"
            } else {
                dataBaseFilePath = "database/en_layouts.db"
            }
            context.deleteDatabase("LD_DATABASE")  //stupid solution need to fix in future (maybe)
            INSTANCE = Room.databaseBuilder(context, AppDB::class.java, "LD_DATABASE")
                .createFromAsset(dataBaseFilePath).build()
        }

        fun getLayoutDB(): AppDB {
            return INSTANCE
        }
    }
}