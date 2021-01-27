package com.test.runar.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    private const val TestServer ="https://runar-testing.herokuapp.com/"
    private const val MainServer ="https://runar-main.herokuapp.com/"

    val retrofitClient = Retrofit.Builder()
        .baseUrl(TestServer+"api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiInterface = retrofitClient.create(BackendApiInterface::class.java)
}