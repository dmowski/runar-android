package com.tnco.runar.repository.backend

import com.tnco.runar.data.remote.BackendApi
import com.tnco.runar.data.remote.RetrofitClient
import com.tnco.runar.data.remote.RunesResponse
import com.tnco.runar.data.remote.request.UserInfo
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.retrofit.BackgroundInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class BackendRepository @Inject constructor(
    private val preferencesRepository: SharedPreferencesRepository,
    private val databaseRepository: DatabaseRepository,
    private val backendApi: BackendApi
) {

    fun identify(userInfo: UserInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                backendApi.createUser(userInfo)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    suspend fun getLibraryData(lang: String) {
        //  RunarLogger.logDebug("Start updating library")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                //RunarLogger.logDebug("Send hash request")
                val hashResp = backendApi.getLibraryHash(lang)
                if (hashResp.isSuccessful) {
                    //RunarLogger.logDebug("Hash GET!: ")
                    val oldHash = preferencesRepository.getLibHash(lang)
                    val newHash = hashResp.body()?.hash
                    // RunarLogger.logDebug("oldHash: $oldHash  newHash: $newHash")
                    if (newHash != null) {
                        if (oldHash != newHash) {
                            // RunarLogger.logDebug("Accepted and started library updating")
                            val response = backendApi.getLibraryData(lang)
                            if (response.isSuccessful) {
                                // RunarLogger.logDebug("Library success: " + response.message().toString())
                                val convertedResult =
                                    DataClassConverters.libRespToItems(response.body()!!)
                                if (Locale.getDefault().language == lang) {
                                    // RunarLogger.logDebug("Data Loaded and Converted")
                                    databaseRepository.updateLibraryDB(convertedResult)
                                    //  RunarLogger.logDebug("save new hash")
                                    preferencesRepository.putLibHash(lang, newHash)
                                } else {
                                    // RunarLogger.logDebug("Language changed can't update db")
                                }
                                // RunarLogger.logDebug("work with library done")
                            } else {
                                //  RunarLogger.logDebug("Library not success: " + response.code().toString())
                            }
                        } else {
                            // RunarLogger.logDebug("Library Data is actual, not need to update")
                        }
                    }
                }
            } catch (e: HttpException) {
                // RunarLogger.logDebug("Library http error")
            } catch (e: Throwable) {
                // RunarLogger.logDebug("Library some strange error")
                // RunarLogger.logError("Library", e)
            }
        }
    }

    suspend fun getRunes(): Response<List<RunesResponse>> {
        return RetrofitClient.apiInterfaceGenerator.getRunes()
    }

    suspend fun getBackgroundInfo(): Response<List<BackgroundInfo>> {
        return RetrofitClient.apiInterface.getBackgroundInfo()
    }

    suspend fun getBackgroundImage(
        runePath: String,
        imgPath: String,
        stylePath: String,
        width: Int,
        height: Int
    ): Response<ResponseBody> {
        return RetrofitClient.apiInterface.getBackgroundImage(
            runePath,
            imgPath,
            stylePath,
            width,
            height
        )
    }

    suspend fun getRunePattern(runesPath: String): Response<List<String>> {
        return RetrofitClient.apiInterfaceGenerator.getRunePattern(runesPath)
    }

    suspend fun getRuneImage(runePath: String, imgPath: String): Response<ResponseBody> {
        return RetrofitClient.apiInterfaceGenerator.getRunePatternImage(runePath, imgPath)
    }
}
