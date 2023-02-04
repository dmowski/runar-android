package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(
    val analyticsHelper: AnalyticsHelper
) : ViewModel() {

    private var _currentPosition = SingleLiveEvent<Int>()
    var currentPosition: LiveData<Int> = _currentPosition
    private var _end = SingleLiveEvent<Boolean>()
    var end: LiveData<Boolean> = _end

    fun changeCurrentPosition(pos: Int) {
        _currentPosition.postValue(pos)
    }

    fun shouldCloseScreen() {
        _end.postValue(true)
    }
}
