package com.test.runar.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.runar.model.*

@Database(
    entities = [DescriptionStatusModel::class,UserLayoutModel::class],
    version = 1,
    exportSchema = false
)
abstract class DataDB : RoomDatabase() {
    abstract fun dataDAO(): DataDAO

    companion object {
        @Volatile
        private lateinit var INSTANCE: DataDB

        fun init(context: Context) {
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(context, DataDB::class.java, "LDD_DATABASE")
                    .createFromAsset("database/app_data.db").build()
            }
        }

        fun getDataDB(): DataDB {
            return INSTANCE
        }
    }
}