package com.tnco.runar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tnco.runar.model.*

@Database(
    entities = [
        LayoutDescriptionModel::class, RuneDescriptionModel::class,
        AffimDescriptionModel::class, TwoRunesInterModel::class,
        LibraryItemsModel::class, RunesItemsModel::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {

        private const val RU_LOCALE = "ru"
        private const val EN_LOCALE = "en"
        private const val RU_DB_NAME = "RU_DATABASE"
        private const val EN_DB_NAME = "EN_DATABASE"
        private const val RU_DB_FILE_NAME = "database/layouts.db"
        private const val EN_DB_FILE_NAME = "database/en_layouts.db"

        private val INSTANCES = mutableMapOf<String, AppDb?>()
        private val dbData = mapOf(
            RU_LOCALE to AppDbData(name = RU_DB_NAME, sourceAssetFileName = RU_DB_FILE_NAME),
            EN_LOCALE to AppDbData(name = EN_DB_NAME, sourceAssetFileName = EN_DB_FILE_NAME)
        )

        private fun isDbExists(context: Context, dbName: String): Boolean {
            val dbFile = context.getDatabasePath(dbName)
            return dbFile.exists()
        }

        private fun createDbFromAsset(context: Context, dbData: AppDbData) =
            Room.databaseBuilder(
                context,
                AppDb::class.java,
                dbData.name
            )
                .createFromAsset(dbData.sourceAssetFileName)
                .addMigrations(MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                .build()

        private fun getDbFromFile(context: Context, dbName: String) =
            Room.databaseBuilder(
                context,
                AppDb::class.java,
                dbName
            ).build()

        fun getInstance(context: Context, language: String): AppDb =
            INSTANCES[language] ?: synchronized(this) {
                INSTANCES[language] ?: run {
                    val appDb = dbData[language]?.let { dbData ->
                        if (isDbExists(context = context, dbName = dbData.name)) {
                            getDbFromFile(context = context, dbName = dbData.name)
                        } else {
                            createDbFromAsset(context = context, dbData = dbData)
                        }
                    } ?: throw RuntimeException("Locale '$language' is not supported")
                    INSTANCES[language] = appDb
                    appDb
                }
            }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE runes_generator " +
                        "(" +
                        "id INTEGER PRIMARY KEY NOT NULL, " +
                        "imgUrl TEXT, " +
                        "enTitle TEXT, " +
                        "ruTitle TEXT" +
                        ")"
                )
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE library ADD COLUMN audio_url TEXT")
                database.execSQL("ALTER TABLE library ADD COLUMN audio_duration INTEGER")
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE library ADD COLUMN rune_tags TEXT")
            }
        }
    }
}
