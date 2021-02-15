package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.test.runar.extensions.SingleLiveEvent
import com.test.runar.repository.DatabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LayoutViewModel : ViewModel() {

    private var _showStatus = SingleLiveEvent<Int>()
    val showStatus : LiveData<Int> = _showStatus


    fun descriptionCheck(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _showStatus.postValue(DatabaseRepository.getShowStatus(id))
        }
    }

}