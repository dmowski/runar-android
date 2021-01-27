package com.test.runar.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BackendApiInterface {
    @POST("create-user")
    suspend fun createUser(@Body user: UserInfo) : Response<UserResponse>
}