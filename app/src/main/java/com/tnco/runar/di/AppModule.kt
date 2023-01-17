package com.tnco.runar.di

import com.tnco.runar.data.remote.LibraryApi
import com.tnco.runar.data.remote.GeneratorApi
import com.tnco.runar.data.remote.RetrofitClient
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
    fun provideLibraryApi(
        retrofitClient: RetrofitClient
    ): LibraryApi = retrofitClient.getLibraryApi()

    @Provides
    @Singleton
    fun provideGeneratorApi(
        retrofitClient: RetrofitClient
    ): GeneratorApi = retrofitClient.getGeneratorApi()
}
