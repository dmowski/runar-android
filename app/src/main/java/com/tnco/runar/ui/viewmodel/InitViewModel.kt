package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.model.LayoutDescriptionModel
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    val analyticsHelper: AnalyticsHelper,
    sharedDataRepository: SharedDataRepository,
) : ViewModel() {

    private val _selectedLayout = SingleLiveEvent<LayoutDescriptionModel>()
    val fontSize = sharedDataRepository.fontSize
    val selectedLayout: LiveData<LayoutDescriptionModel> = _selectedLayout

    fun getLayoutDescription(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _selectedLayout.postValue(databaseRepository.getLayoutDetails(id))
        }
    }
}
