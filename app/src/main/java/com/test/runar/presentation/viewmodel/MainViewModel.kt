package com.test.runar.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.repository.LayoutDescriptionRepository
import com.test.runar.repository.SharedPreferencesRepository
import com.test.runar.retrofit.RetrofitClient
import com.test.runar.retrofit.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var selectedLayout = MutableLiveData<LayoutDescriptionModel>(null)
    var showStatus = MutableLiveData(3)
    var preferencesRepository = SharedPreferencesRepository(application)

    fun notShowSelectedLayout(context: Context, id: Int) {
        CoroutineScope(IO).launch {
            LayoutDescriptionRepository.notShow(context, id)
        }
    }

    fun getLayoutDescription(context: Context, id: Int) {
        CoroutineScope(IO).launch {
            selectedLayout.postValue(LayoutDescriptionRepository.getLayoutDetails(context, id))
        }
    }

    fun clearLayoutData() {
        selectedLayout.postValue(null)
    }

    fun descriptionCheck(context: Context, id: Int) {
        CoroutineScope(IO).launch {
            showStatus.postValue(LayoutDescriptionRepository.getShowStatus(context, id))
        }
    }

    fun clearShowStatus() {
        showStatus.postValue(3)
    }

    fun identify() {
        var userId = preferencesRepository.getUserId()
        var timeStamp = System.currentTimeMillis() / 1000L
        var androidVersion = "Android " + Build.VERSION.RELEASE
        Log.d("Log", androidVersion)
        CoroutineScope(IO).launch {
            try {
                val response = RetrofitClient.apiInterface.createUser(UserInfo(userId, timeStamp, androidVersion))
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