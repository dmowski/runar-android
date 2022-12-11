package com.tnco.runar.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitClient @Inject constructor() {

    companion object {
        private const val TestServer = "https://runar-testing.herokuapp.com/"
        private const val MainServer = "https://runar-main.herokuapp.com/"
        private const val GeneratorServer = "https://runar-generator-api.herokuapp.com/"
    }

    fun getLibraryApi(): LibraryApi = getLibraryClient().create(LibraryApi::class.java)

    fun getGeneratorApi(): GeneratorApi =
        getGeneratorClient().create(GeneratorApi::class.java)

    private val client = OkHttpClient.Builder()
        .connectTimeout(40, TimeUnit.SECONDS)
        .writeTimeout(40, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)
        .build()

    private fun getLibraryClient(): Retrofit = Retrofit.Builder()
        .baseUrl(MainServer + "api/v1/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getGeneratorClient(): Retrofit = Retrofit.Builder()
        .baseUrl(GeneratorServer + "api/v1/")
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
