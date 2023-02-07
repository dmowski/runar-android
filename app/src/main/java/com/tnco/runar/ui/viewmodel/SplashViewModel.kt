package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.repository.LanguageRepository
import com.tnco.runar.repository.backend.BackendRepository
import com.tnco.runar.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val backendRepository: BackendRepository,
    private val languageRepository: LanguageRepository
) : ViewModel() {

    private val _splashCommand = SingleLiveEvent<Boolean>()
    private val _progress = MutableLiveData<Int>()

    val splashCommand: LiveData<Boolean> = _splashCommand
    val progress: LiveData<Int> = _progress

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val backendConnection = true

            delay(DELAY_BEFORE_START_LOADING)
            repeat(2) {
                delay(STEP_OF_LOADING)
                _progress.postValue(25 * (it + 1))
            }
            if (backendConnection) {
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
            _splashCommand.postValue(true)
        }
    }

    private companion object {
        const val DELAY_BEFORE_START_LOADING = 500L
        const val STEP_OF_LOADING = 500L
    }
}
