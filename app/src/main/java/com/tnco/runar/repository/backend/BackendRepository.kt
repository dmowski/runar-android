package com.tnco.runar.repository.backend

import com.tnco.runar.data.remote.RetrofitClient
import com.tnco.runar.data.remote.UserInfo
import com.tnco.runar.model.RunesItemsModel
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.tnco.runar.data.remote.RunesResponse
import com.tnco.runar.retrofit.BackgroundInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception
import java.util.*

object BackendRepository {

    fun identify(userInfo: UserInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiInterface.createUser(userInfo)
                if (response.isSuccessful) {
                    //RunarLogger.logDebug("Identification success: " + response.message().toString())
                } else {
                    //RunarLogger.logDebug( "Identification not success: " + response.code().toString() )
                }
            } catch (e: HttpException) {
                //RunarLogger.logDebug("Identification http error")
            } catch (e: Throwable) {
                //RunarLogger.logDebug("Identification some strange error")
            }
        }
    }

    suspend fun getLibraryData(lang: String) {
      //  RunarLogger.logDebug("Start updating library")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                //RunarLogger.logDebug("Send hash request")
                val hashResp = RetrofitClient.apiInterface.getLibraryHash(lang)
                if (hashResp.isSuccessful) {
                    //RunarLogger.logDebug("Hash GET!: ")
                    val spr = SharedPreferencesRepository.get()
                    val oldHash = spr.getLibHash(lang)
                    val newHash = hashResp.body()?.hash
                    //RunarLogger.logDebug("oldHash: $oldHash  newHash: $newHash")
                    if (newHash != null) {
                        if (oldHash != newHash) {
                          // RunarLogger.logDebug("Accepted and started library updating")
                            val response = RetrofitClient.apiInterface.getLibraryData(lang)
                            if (response.isSuccessful) {
                               // RunarLogger.logDebug("Library success: " + response.message().toString())
                                val convertedResult =
                                    DataClassConverters.libRespToItems(response.body()!!)
                                if (Locale.getDefault().language == lang) {
                                   // RunarLogger.logDebug("Data Loaded and Converted")
                                    DatabaseRepository.updateLibraryDB(convertedResult)
                                  //  RunarLogger.logDebug("save new hash")
                                    spr.putLibHash(lang, newHash)
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
                //RunarLogger.logDebug("Library http error")
            } catch (e: Throwable) {
                //RunarLogger.logDebug("Library some strange error")
                //RunarLogger.logError("Library", e)
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
        return RetrofitClient.apiInterface.getBackgroundImage(runePath, imgPath, stylePath, width, height)
    }

    suspend fun getRunePattern(runesPath: String): Response<List<String>> {
        return RetrofitClient.apiInterfaceGenerator.getRunePattern(runesPath)
    }

    suspend fun getRuneImage(runePath: String,imgPath: String): Response<ResponseBody> {
        return RetrofitClient.apiInterfaceGenerator.getRunePatternImage(runePath,imgPath)
    }
}