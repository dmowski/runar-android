package com.tnco.runar.data.remote

import com.tnco.runar.data.remote.response.RunesResponse
import com.tnco.runar.data.remote.request.BackgroundInfo
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GeneratorApi {

    @GET("runes")
    suspend fun getRunes(): Response<List<RunesResponse>>

    // Берём список узоров из рун
    @GET("empty-wallpapers/{stringPath}")
    suspend fun getRunePattern(@Path("stringPath") stringPath: String): Response<List<String>>

    @GET("empty-wallpapers/{numberPath}/{imgPath}")
    suspend fun getRunePatternImage(
        @Path("numberPath") numberPath: String,
        @Path("imgPath") imgPath: String
    ): Response<ResponseBody>

    // Берём список фонов
    @GET("https://runar-generator-api.herokuapp.com/api/v1/wallpapersStyles")
    suspend fun getBackgroundInfo(): Response<List<BackgroundInfo>>

    @GET("https://runar-generator-api.herokuapp.com/api/v1/wallpapers/{runePath}/{imgPath}.png")
    suspend fun getBackgroundImage(
        @Path("runePath") runePath: String,
        @Path("imgPath") imgPath: String,
        @Query("style") stylePath: String,
        @Query("width") width: Int,
        @Query("height") height: Int
    ): Response<ResponseBody>
}
