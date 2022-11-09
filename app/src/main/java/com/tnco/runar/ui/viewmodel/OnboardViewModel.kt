package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.util.SingleLiveEvent

class OnboardViewModel : ViewModel() {

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