package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.extensions.SingleLiveEvent
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.repository.DatabaseRepository
import com.test.runar.repository.SharedDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InitViewModel : ViewModel() {

    private var _selectedLayout = SingleLiveEvent<LayoutDescriptionModel>()
    private var _fontSize = MutableLiveData<Float>()
    val selectedLayout: LiveData<LayoutDescriptionModel> = _selectedLayout
    val fontSize: LiveData<Float> = _fontSize

    init {
        _fontSize.postValue(SharedDataRepository.fontSize)
    }


    fun getLayoutDescription(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _selectedLayout.postValue(DatabaseRepository.getLayoutDetails(id))
        }
    }
}