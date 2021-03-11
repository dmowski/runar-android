package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.runar.RunarLogger
import com.test.runar.extensions.SingleLiveEvent
import com.test.runar.repository.BackendRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class SplashViewModel : ViewModel() {

    private val _splashCommand = SingleLiveEvent<Boolean>()
    private val _progress = MutableLiveData<Int>()

    val splashCommand: LiveData<Boolean> = _splashCommand
    val progress: LiveData<Int> = _progress

    init {
        CoroutineScope(Dispatchers.IO).launch {

            delay(DELAY_BEFORE_START_LOADING)
            repeat(2){
                delay(STEP_OF_LOADING)
                RunarLogger.logDebug("1")
                _progress.postValue(25 * (it + 1))
            }
            val locale: String = Locale.getDefault().language
            if (locale.equals("ru"))  BackendRepository.getLibraryData("ru")
            else  BackendRepository.getLibraryData("en")
            repeat(2){
                delay(STEP_OF_LOADING)
                RunarLogger.logDebug("2")
                _progress.postValue(25 * (it + 2))
            }
            _splashCommand.postValue(true)
        }
    }

    private companion object{
        const val DELAY_BEFORE_START_LOADING = 500L
        const val STEP_OF_LOADING = 500L
    }
}