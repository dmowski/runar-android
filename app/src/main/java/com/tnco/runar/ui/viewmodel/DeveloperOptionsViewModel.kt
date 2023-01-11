package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnco.runar.model.DeveloperSwitcher
import com.tnco.runar.repository.data_store.DataStorePreferences
import com.tnco.runar.repository.SharedDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DeveloperOptionsViewModel() : ViewModel() {

    init {
        viewModelScope.launch {
            val switcherStates = DataStorePreferences.switchers.first()
            if (switcherStates.isEmpty()) {
                DataStorePreferences.initialPopulate()
            }
        }
    }

    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    val switchers: Flow<List<DeveloperSwitcher>> = DataStorePreferences.switchers

    fun saveSwitcher(switcher: DeveloperSwitcher) {
        viewModelScope.launch {
            DataStorePreferences.saveSwitcher(switcher)
        }
    }
}
