package com.tnco.runar.di

import com.tnco.runar.data.remote.LibraryApi
import com.tnco.runar.data.remote.GeneratorApi
import com.tnco.runar.data.remote.RetrofitClient
import com.tnco.runar.repository.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideDatabaseRepository(): DatabaseRepository = DatabaseRepository

    @Provides
    @Singleton
    fun provideBackendApi(
        retrofitClient: RetrofitClient
    ): LibraryApi = retrofitClient.getLibraryApi()

    @Provides
    @Singleton
    fun provideGeneratorApi(
        retrofitClient: RetrofitClient
    ): GeneratorApi = retrofitClient.getGeneratorApi()
}
