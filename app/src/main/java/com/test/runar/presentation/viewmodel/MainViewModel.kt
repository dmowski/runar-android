package com.test.runar.presentation.viewmodel

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.repository.BackendRepository
import com.test.runar.repository.SharedDataRepository
import com.test.runar.repository.SharedPreferencesRepository
import com.test.runar.retrofit.UserInfo

class MainViewModel : ViewModel() {

    var preferencesRepository = SharedPreferencesRepository.get()
    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)

    fun identify() {
        val userId = preferencesRepository.userId
        val timeStamp = System.currentTimeMillis() / 1000L
        val androidVersion = "Android " + Build.VERSION.RELEASE
        BackendRepository.identify(UserInfo(userId,timeStamp,androidVersion))
    }
}