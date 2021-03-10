package com.test.runar.repository

import com.test.runar.RunarLogger
import com.test.runar.converters.DataClassConverters
import com.test.runar.retrofit.RetrofitClient
import com.test.runar.retrofit.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

object BackendRepository {

    fun identify(userInfo: UserInfo){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiInterface.createUser(userInfo)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        RunarLogger.logDebug("Identification success: "+ response.message().toString())
                    } else {
                        RunarLogger.logDebug("Identification not success: "+ response.code().toString())
                    }
                }
            } catch (e: HttpException) {
                RunarLogger.logDebug("Identification http error")
            } catch (e: Throwable) {
                RunarLogger.logDebug("Identification some strange error")
            }
        }
    }
    fun getLibraryData(lang: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiInterface.getLibraryData(lang)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        RunarLogger.logDebug("Library success: "+ response.message().toString())
                        val convertedResult = DataClassConverters.LibRespToItems(response.body()!!)
                        for(item in convertedResult) RunarLogger.logDebug(item.toString())
                    } else {
                        RunarLogger.logDebug("Library not success: "+ response.code().toString())
                    }
                }
            } catch (e: HttpException) {
                RunarLogger.logDebug("Library http error")
            } catch (e: Throwable) {
                RunarLogger.logDebug("Library some strange error")
            }
        }
    }
}