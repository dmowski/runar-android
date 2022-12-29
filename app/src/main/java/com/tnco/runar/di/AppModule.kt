package com.tnco.runar.di

import com.tnco.runar.data.remote.LibraryApi
import com.tnco.runar.data.remote.GeneratorApi
import com.tnco.runar.data.remote.RetrofitClient
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.LanguageRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.repository.backend.BackendRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDatabaseRepository(): DatabaseRepository = DatabaseRepository

    @Provides
    @Singleton
    fun provideLibraryApi(
        retrofitClient: RetrofitClient
    ): LibraryApi = retrofitClient.getLibraryApi()

    @Provides
    @Singleton
    fun provideGeneratorApi(
        retrofitClient: RetrofitClient
    ): GeneratorApi = retrofitClient.getGeneratorApi()

    @Provides
    @Singleton
    fun provideLanguageRepository(
        preferencesRepository: SharedPreferencesRepository,
        sharedDataRepository: SharedDataRepository,
        databaseRepository: DatabaseRepository
    ): LanguageRepository =
        LanguageRepository(preferencesRepository, sharedDataRepository, databaseRepository)

    @Provides
    @Singleton
    fun provideBackendRepository(
        preferencesRepository: SharedPreferencesRepository,
        databaseRepository: DatabaseRepository,
        libraryApi: LibraryApi,
        generatorApi: GeneratorApi
    ): BackendRepository =
        BackendRepository(preferencesRepository, databaseRepository, libraryApi, generatorApi)
}
