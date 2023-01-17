package com.tnco.runar.di

import android.content.Context
import androidx.room.Room
import com.tnco.runar.data.local.DataDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDataDB(@ApplicationContext context: Context): DataDB {
        return Room.databaseBuilder(
            context,
            DataDB::class.java,
            "LDD_DATABASE"
        ).createFromAsset(
            "database/app_data.db"
        ).build()
    }
}
