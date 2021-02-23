package com.test.runar.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.runar.model.*
import java.util.*

@Database(
    entities = [DescriptionStatusModel::class,UserLayoutModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataDB : RoomDatabase() {
    abstract fun dataDAO(): DataDAO

    companion object {
        @Volatile
        private lateinit var INSTANCE: AppDataDB

        fun init(context: Context) {
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(context, AppDataDB::class.java, "LDD_DATABASE")
                    .createFromAsset("app_data.db").build()
            }
        }

        fun getDataDB(): AppDataDB {
            return INSTANCE
        }
    }
}