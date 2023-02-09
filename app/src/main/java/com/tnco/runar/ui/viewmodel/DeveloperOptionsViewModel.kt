package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnco.runar.model.DeveloperSwitcher
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.data_store.DataStorePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeveloperOptionsViewModel @Inject constructor(
    private val dataStorePreferences: DataStorePreferences,
    sharedDataRepository: SharedDataRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            val switcherStates = dataStorePreferences.switchers.first()
            if (switcherStates.isEmpty()) {
                dataStorePreferences.initialPopulate()
            }
        }
    }

    val fontSize: LiveData<Float> = sharedDataRepository.fontSize
    val switchers: Flow<List<DeveloperSwitcher>> = dataStorePreferences.switchers

    fun saveSwitcher(switcher: DeveloperSwitcher) {
        viewModelScope.launch {
            dataStorePreferences.saveSwitcher(switcher)
        }
    }
}
