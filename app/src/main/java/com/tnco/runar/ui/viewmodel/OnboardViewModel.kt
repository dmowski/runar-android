package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(
    sharedDataRepository: SharedDataRepository
) : ViewModel() {
    val fontSize: LiveData<Float> = MutableLiveData(sharedDataRepository.fontSize)
    private var _currentPosition = SingleLiveEvent<Int>()
    var currentPosition: LiveData<Int> = _currentPosition
    private var _end = SingleLiveEvent<Boolean>()
    var end: LiveData<Boolean> = _end

    fun changeCurrentPosition(pos: Int) {
        _currentPosition.postValue(pos)
    }

    fun nextActivity(status: Boolean) {
        _end.postValue(status)
    }
}