package com.tnco.runar.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface BackendApiInterface {
    @POST("create-user")
    suspend fun createUser(@Body user: UserInfo): Response<UserResponse>
    @GET("library/{language}")
    suspend fun getLibraryData(@Path("language") language: String): Response<List<LibraryResponse>>
    @GET("library-hash/{language}")
    suspend fun getLibraryHash(@Path("language") language: String): Response<HashResponse>

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
