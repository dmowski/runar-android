package com.tnco.runar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tnco.runar.model.*
import java.util.*

@Database(
    entities = [LayoutDescriptionModel::class, RuneDescriptionModel::class, AffimDescriptionModel::class, TwoRunesInterModel::class, LibraryItemsModel::class, RunesItemsModel::class],
    version = 4,
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
            var dataBaseName = ""

            if (locale.equals("ru")) {
                dataBaseFilePath = "database/layouts.db"
                dataBaseName = "RU_DATABASE"
            } else {
                dataBaseFilePath = "database/en_layouts.db"
                dataBaseName = "EN_DATABASE"
            }
            INSTANCE = Room.databaseBuilder(context, AppDB::class.java, dataBaseName)
                .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
                .createFromAsset(dataBaseFilePath).build()
        }

        fun getLayoutDB(): AppDB {
            return INSTANCE
        }


        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE runes_generator (id INTEGER PRIMARY KEY NOT NULL, imgUrl TEXT, enTitle TEXT, ruTitle TEXT)")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE library ADD COLUMN audio_url TEXT")
                database.execSQL("ALTER TABLE library ADD COLUMN audio_duration INTEGER")
            }
        }

    }
}
