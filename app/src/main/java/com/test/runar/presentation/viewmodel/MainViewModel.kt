package com.test.runar.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.test.runar.repository.SharedPreferencesRepository
import com.test.runar.retrofit.RetrofitClient
import com.test.runar.retrofit.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainViewModel : ViewModel() {

    var preferencesRepository = SharedPreferencesRepository.get()

    fun identify() {
        val userId = preferencesRepository.userId
        val timeStamp = System.currentTimeMillis() / 1000L
        val androidVersion = "Android " + Build.VERSION.RELEASE
        Log.d("Log", androidVersion)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiInterface.createUser(
                        UserInfo(
                                userId,
                                timeStamp,
                                androidVersion
                        )
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Log.d("Log", response.message().toString())
                    } else {
                        Log.d("Log", response.code().toString())
                    }
                }
            } catch (e: HttpException) {
                Log.d("Log", "Http error ")
            } catch (e: Throwable) {
                Log.d("Log", "Some Error ")
            }
        }
    }

}