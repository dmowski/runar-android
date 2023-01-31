package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProcessingViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    val analyticsHelper: AnalyticsHelper,
    sharedDataRepository: SharedDataRepository
) : ViewModel() {

    private var _layoutName = SingleLiveEvent<String>()
    val layoutName: LiveData<String> = _layoutName
    val fontSize: LiveData<Float> = MutableLiveData(sharedDataRepository.fontSize)

    fun getLayoutName(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _layoutName.postValue(databaseRepository.getLayoutName(id))
        }
    }
}
