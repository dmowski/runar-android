package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.util.SingleLiveEvent
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