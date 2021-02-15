package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.test.runar.extensions.SingleLiveEvent
import com.test.runar.repository.DatabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProcessingViewModel : ViewModel() {

    private var _layoutName = SingleLiveEvent<String>()
    val layoutName : LiveData<String> = _layoutName


    fun getLayoutName(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _layoutName.postValue(DatabaseRepository.getLayoutName(id))
        }
    }
}