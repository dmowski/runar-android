package com.tnco.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.extensions.SingleLiveEvent
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.SharedPreferencesRepository

class OnboardViewModel : ViewModel() {
    var preferencesRepository = SharedPreferencesRepository.get()
    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    private var _currentPosition = SingleLiveEvent<Int>()
    var currentPosition : LiveData<Int> = _currentPosition
    private var _end = SingleLiveEvent<Boolean>()
    var end : LiveData<Boolean> = _end

    fun changeCurrentPosition(pos: Int){
        _currentPosition.postValue(pos)
    }
    fun nextActivity(status: Boolean){
        _end.postValue(status)
    }
}