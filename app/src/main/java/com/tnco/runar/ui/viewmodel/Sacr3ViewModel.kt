package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.repository.SharedDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Sacr3ViewModel @Inject constructor(
    sharedDataRepository: SharedDataRepository
) : ViewModel() {

    private var _fontSize = MutableLiveData<Float>()
    val fontSize: LiveData<Float> = _fontSize

    init {
        _fontSize.postValue(sharedDataRepository.fontSize)
    }
}
