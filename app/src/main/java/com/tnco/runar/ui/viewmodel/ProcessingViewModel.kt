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

class ProcessingViewModel : ViewModel() {

    private var _layoutName = SingleLiveEvent<String>()
    val layoutName : LiveData<String> = _layoutName
    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)


    fun getLayoutName(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _layoutName.postValue(DatabaseRepository.getLayoutName(id))
        }
    }
}