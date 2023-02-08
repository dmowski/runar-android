package com.tnco.runar.di.modules

import com.tnco.runar.data.remote.BackendApiInterface
import com.tnco.runar.di.annotations.GeneratorServer
import com.tnco.runar.di.annotations.MainServer
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TEST_SERVER_URL = "https://runar-testing.herokuapp.com/" // TODO is it necessary?
    private const val MAIN_SERVER_URL = "https://runar-main.herokuapp.com/"
    private const val GENERATOR_SERVER_URL = "https://runar-generator-api.herokuapp.com/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(40, TimeUnit.SECONDS)
        .writeTimeout(40, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)
        .build()

    private val retrofitClientMain: Retrofit = Retrofit.Builder()
        .baseUrl(MAIN_SERVER_URL + "api/v1/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitClientGenerator: Retrofit = Retrofit.Builder()
        .baseUrl(GENERATOR_SERVER_URL + "api/v1/")
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @MainServer
    @Reusable
    @Provides
    fun provideBackendApiInterfaceMain(): BackendApiInterface =
        retrofitClientMain.create(BackendApiInterface::class.java)

    @GeneratorServer
    @Reusable
    @Provides
    fun provideBackendApiInterfaceGenerator(): BackendApiInterface =
        retrofitClientGenerator.create(BackendApiInterface::class.java)
}
