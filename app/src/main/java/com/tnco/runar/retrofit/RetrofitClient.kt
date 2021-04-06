package com.tnco.runar.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val TestServer ="https://runar-testing.herokuapp.com/"
    private const val MainServer ="https://runar-main.herokuapp.com/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .build()

    private val retrofitClient: Retrofit = Retrofit.Builder()
        .baseUrl(MainServer+"api/v1/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiInterface: BackendApiInterface = retrofitClient.create(BackendApiInterface::class.java)
}