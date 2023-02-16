package com.tnco.runar.repository.backend

import com.tnco.runar.data.remote.BackendApiInterface
import com.tnco.runar.data.remote.BackgroundInfo
import com.tnco.runar.data.remote.RunesResponse
import com.tnco.runar.data.remote.UserInfo
import com.tnco.runar.di.annotations.GeneratorServer
import com.tnco.runar.di.annotations.MainServer
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class BackendRepository @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    @MainServer
    private val backendApiInterfaceMain: BackendApiInterface,
    @GeneratorServer
    private val backendApiInterfaceGenerator: BackendApiInterface
) {

    fun identify(userInfo: UserInfo) { // TODO make suspend with viewModelScope
        CoroutineScope(Dispatchers.IO).launch { // TODO get rid of the dispatcher, Retrofit uses a non-UI dispatcher under the hood
            try {
                val response = backendApiInterfaceMain.createUser(userInfo)
                if (response.isSuccessful) {
                    // RunarLogger.logDebug("Identification success: " + response.message().toString())
                } else {
                    // RunarLogger.logDebug( "Identification not success: " + response.code().toString() )
                }
            } catch (e: HttpException) {
                // RunarLogger.logDebug("Identification http error")
            } catch (e: Throwable) {
                // RunarLogger.logDebug("Identification some strange error")
            }
        }
    }

    suspend fun getLibraryData(lang: String) {
        //  RunarLogger.logDebug("Start updating library")
        CoroutineScope(Dispatchers.IO).launch { // TODO get rid of the scope
            try {
                // RunarLogger.logDebug("Send hash request")
                val hashResp = backendApiInterfaceMain.getLibraryHash(lang)
                if (hashResp.isSuccessful) {
                    // RunarLogger.logDebug("Hash GET!: ")
                    val oldHash = sharedPreferencesRepository.getLibHash(lang)
                    val newHash = hashResp.body()?.hash
                    // RunarLogger.logDebug("oldHash: $oldHash  newHash: $newHash")
                    if (newHash != null) {
                        if (oldHash != newHash) {
                            // RunarLogger.logDebug("Accepted and started library updating")
                            val response = backendApiInterfaceMain.getLibraryData(lang)
                            if (response.isSuccessful) {
                                // RunarLogger.logDebug("Library success: " + response.message().toString())
                                val convertedResult =
                                    DataClassConverters.libRespToItems(response.body()!!)
                                if (Locale.getDefault().language == lang) {
                                    // RunarLogger.logDebug("Data Loaded and Converted")
                                    databaseRepository.updateLibraryDB(convertedResult)
                                    //  RunarLogger.logDebug("save new hash")
                                    sharedPreferencesRepository.putLibHash(lang, newHash)
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
        return backendApiInterfaceGenerator.getRunes()
    }

    suspend fun getBackgroundInfo(): Response<List<BackgroundInfo>> {
        return backendApiInterfaceMain.getBackgroundInfo()
    }

    suspend fun getBackgroundImage(
        runePath: String,
        imgPath: String,
        stylePath: String,
        width: Int,
        height: Int
    ): Response<ResponseBody> {
        return backendApiInterfaceMain.getBackgroundImage(
            runePath,
            imgPath,
            stylePath,
            width,
            height
        )
    }

    suspend fun getRunePattern(runesPath: String): Response<List<String>> {
        return backendApiInterfaceGenerator.getRunePattern(runesPath)
    }

    suspend fun getRuneImage(runePath: String, imgPath: String): Response<ResponseBody> {
        return backendApiInterfaceGenerator.getRunePatternImage(runePath, imgPath)
    }
}
