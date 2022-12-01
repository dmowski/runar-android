package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.repository.SharedDataRepository

class Sacr3ViewModel : ViewModel() {

    private var _fontSize = MutableLiveData<Float>()
    val fontSize: LiveData<Float> = _fontSize

    init {
        _fontSize.postValue(SharedDataRepository.fontSize)
    }
}
