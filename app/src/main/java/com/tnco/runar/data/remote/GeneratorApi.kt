package com.tnco.runar.data.remote

import com.tnco.runar.data.remote.response.RunesResponse
import retrofit2.Response
import retrofit2.http.GET

interface GeneratorApi {
    @GET("runes")
    suspend fun getRunes() : Response<List<RunesResponse>>
}