package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LayoutViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private var _showStatus = SingleLiveEvent<Boolean>()
    val showStatus: LiveData<Boolean> = _showStatus
    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)

    fun checkDescriptionStatus(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _showStatus.postValue(databaseRepository.getShowStatus(id) == 1)
        }
    }
}
