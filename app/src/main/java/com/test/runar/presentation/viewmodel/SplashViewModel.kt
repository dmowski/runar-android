package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.runar.extensions.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _splashCommand = SingleLiveEvent<Boolean>()
    private val _progress = MutableLiveData<Int>()

    val splashCommand: LiveData<Boolean> = _splashCommand
    val progress: LiveData<Int> = _progress

    init {
        viewModelScope.launch {
            delay(DELAY_BEFORE_START_LOADING)
            repeat(4){
                _progress.postValue(25 * (it + 1))
                delay(STEP_OF_LOADING)
            }
            _splashCommand.postValue(true)
        }
    }

    private companion object{
        const val DELAY_BEFORE_START_LOADING = 1000L
        const val STEP_OF_LOADING = 500L
    }
}