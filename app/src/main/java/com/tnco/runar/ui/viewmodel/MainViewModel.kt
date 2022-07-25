package com.tnco.runar.ui.viewmodel

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.data.remote.request.UserInfo
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.repository.backend.BackendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesRepository: SharedPreferencesRepository,
    private val backendRepository: BackendRepository,
    sharedDataRepository: SharedDataRepository
) : ViewModel() {

    val fontSize: LiveData<Float> = MutableLiveData(sharedDataRepository.fontSize)

    fun identify() {
        backendRepository.identify(
            UserInfo(
                preferencesRepository.userId,
                System.currentTimeMillis() / 1000L,
                "Android " + Build.VERSION.RELEASE
            )
        )
    }
}