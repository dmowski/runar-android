package com.test.runar.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BackendApiInterface {
    @POST("create-user")
    suspend fun createUser(@Body user: UserInfo) : Response<UserResponse>
    @GET("library/{language}")
    suspend fun getLibraryData(@Path("language") language: String) : Response<List<LibraryResponse>>
    @GET("library-hash/{language}")
    suspend fun getLibraryHash(@Path("language") language: String) : Response<HashResponse>

}