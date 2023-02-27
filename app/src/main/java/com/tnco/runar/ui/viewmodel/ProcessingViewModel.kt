package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.di.annotations.IoDispatcher
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProcessingViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    val analyticsHelper: AnalyticsHelper,
    sharedDataRepository: SharedDataRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _layoutName = SingleLiveEvent<String>()
    val layoutName: LiveData<String> = _layoutName
    val fontSize: LiveData<Float> = sharedDataRepository.fontSize

    fun getLayoutName(id: Int) {
        viewModelScope.launch(ioDispatcher) {
            _layoutName.postValue(databaseRepository.getLayoutName(id))
        }
    }
}
