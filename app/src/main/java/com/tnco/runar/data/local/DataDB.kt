package com.tnco.runar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tnco.runar.model.DescriptionStatusModel
import com.tnco.runar.model.UserLayoutModel

@Database(
    entities = [DescriptionStatusModel::class, UserLayoutModel::class],
    version = 2,
    exportSchema = false
)

abstract class DataDB : RoomDatabase() {
    abstract fun dataDAO(): DataDao

    companion object {

        private const val DB_NAME = "LDD_DATABASE"
        private const val ASSET_NAME = "database/app_data.db"

        @Volatile
        private var INSTANCE: DataDB? = null

        fun getInstance(context: Context): DataDB {
            INSTANCE?.let {
                return it
            }
            synchronized(this) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    context,
                    DataDB::class.java,
                    DB_NAME
                )
                    .createFromAsset(ASSET_NAME)
                    .build()
                INSTANCE = db
                return db
            }
        }
    }
}
