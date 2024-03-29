package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnco.runar.di.annotations.IoDispatcher
import com.tnco.runar.repository.LanguageRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.repository.backend.BackendRepository
import com.tnco.runar.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val backendRepository: BackendRepository,
    private val languageRepository: LanguageRepository,
    val sharedPreferencesRepository: SharedPreferencesRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _isOnboardToStart = SingleLiveEvent<Boolean>()
    private val _progress = MutableLiveData<Int>()

    val isOnboardToStart: LiveData<Boolean> = _isOnboardToStart
    val progress: LiveData<Int> = _progress

    init {
        viewModelScope.launch(ioDispatcher) {
            val backendConnection = true

            delay(DELAY_BEFORE_START_LOADING)
            repeat(2) {
                delay(STEP_OF_LOADING)
                _progress.postValue(25 * (it + 1))
            }
            if (backendConnection) { // TODO predicate is always true
                if (languageRepository.currentAppLanguage() == "ru") {
                    backendRepository.getLibraryData("ru")
                } else {
                    backendRepository.getLibraryData("en")
                }
            }
            repeat(2) {
                delay(STEP_OF_LOADING)
                _progress.postValue(25 * (it + 2))
            }
            val settingsOnboarding = sharedPreferencesRepository.settingsOnboarding == 1
            _isOnboardToStart.postValue(settingsOnboarding)
        }
    }

    private companion object {
        const val DELAY_BEFORE_START_LOADING = 500L
        const val STEP_OF_LOADING = 500L
    }
}
