package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnco.runar.di.annotations.IoDispatcher
import com.tnco.runar.model.DeveloperSwitcher
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.data_store.DataStorePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeveloperOptionsViewModel @Inject constructor(
    private val dataStorePreferences: DataStorePreferences,
    sharedDataRepository: SharedDataRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val fontSize: LiveData<Float> = sharedDataRepository.fontSize
    val switchers: Flow<List<DeveloperSwitcher>> = dataStorePreferences.switchers

    fun initialPopulate() {
        viewModelScope.launch(ioDispatcher) {
            dataStorePreferences.initialPopulate()
        }
    }

    fun saveSwitcher(switcher: DeveloperSwitcher) {
        viewModelScope.launch(ioDispatcher) {
            dataStorePreferences.saveSwitcher(switcher)
        }
    }
}
