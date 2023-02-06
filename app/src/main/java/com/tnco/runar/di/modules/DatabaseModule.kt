package com.tnco.runar.di.modules

import android.content.Context
import com.tnco.runar.data.local.AppDao
import com.tnco.runar.data.local.AppDb
import com.tnco.runar.data.local.DataDao
import com.tnco.runar.data.local.DataDb
import com.tnco.runar.di.annotations.EnLocale
import com.tnco.runar.di.annotations.RuLocale
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @RuLocale
    @Provides
    fun provideRuAppDao(@ApplicationContext context: Context): AppDao =
        AppDb.getInstance(context = context, language = "ru").appDao()

    @EnLocale
    @Provides
    fun provideEnAppDao(@ApplicationContext context: Context): AppDao =
        AppDb.getInstance(context = context, language = "en").appDao()

    @Provides
    @Reusable
    fun provideDataDao(@ApplicationContext context: Context): DataDao =
        DataDb.getInstance(context = context).dataDAO()
}
