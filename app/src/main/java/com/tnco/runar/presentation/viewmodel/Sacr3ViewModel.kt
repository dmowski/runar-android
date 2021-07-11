package com.tnco.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.extensions.SingleLiveEvent
import com.tnco.runar.model.LayoutDescriptionModel
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Sacr3ViewModel : ViewModel() {

    private var _selectedLayout = SingleLiveEvent<LayoutDescriptionModel>()
    private var _fontSize = MutableLiveData<Float>()
    val fontSize: LiveData<Float> = _fontSize

    init {
        _fontSize.postValue(SharedDataRepository.fontSize)
    }

}