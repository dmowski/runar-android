package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.RunarLogger
import com.test.runar.extensions.SingleLiveEvent
import com.test.runar.repository.DatabaseRepository
import com.test.runar.repository.SharedDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LayoutViewModel : ViewModel() {

    private var _showStatus = SingleLiveEvent<Boolean>()
    val showStatus : LiveData<Boolean> = _showStatus
    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)

    fun checkDescriptionStatus(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _showStatus.postValue(DatabaseRepository.getShowStatus(id) == 1)
        }
    }
}