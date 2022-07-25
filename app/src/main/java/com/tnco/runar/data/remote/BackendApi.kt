package com.tnco.runar.data.remote

import com.tnco.runar.data.remote.request.UserInfo
import com.tnco.runar.data.remote.response.HashResponse
import com.tnco.runar.data.remote.response.LibraryResponse
import com.tnco.runar.data.remote.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BackendApi {
    @POST("create-user")
    suspend fun createUser(@Body user: UserInfo) : Response<UserResponse>
    @GET("library/{language}")
    suspend fun getLibraryData(@Path("language") language: String) : Response<List<LibraryResponse>>
    @GET("library-hash/{language}")
    suspend fun getLibraryHash(@Path("language") language: String) : Response<HashResponse>
}