package com.tnco.runar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tnco.runar.model.DescriptionStatusModel
import com.tnco.runar.model.UserLayoutModel

@Database(
    entities = [DescriptionStatusModel::class, UserLayoutModel::class],
    version = 2,
    exportSchema = false
)

abstract class DataDB : RoomDatabase() {

    abstract fun dataDAO(): DataDAO
}
