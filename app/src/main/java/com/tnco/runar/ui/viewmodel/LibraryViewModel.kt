package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.*
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.di.annotations.IoDispatcher
import com.tnco.runar.domain.entities.LibraryItem
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.data_store.DataStorePreferences
import com.tnco.runar.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    private val databaseRepository: DatabaseRepository,
    val analyticsHelper: AnalyticsHelper,
    sharedDataRepository: SharedDataRepository,
    dataStore: DataStorePreferences,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val fontSize: LiveData<Float> = sharedDataRepository.fontSize
    var currentFragmentTitle = DEFAULT_TITLE
        private set

    private val _errorLoad = MutableLiveData<Boolean>()
    val errorLoad: LiveData<Boolean>
        get() = _errorLoad

    val isOnline = networkMonitor.isConnected.asLiveData()

    private val _libraryItemList: MutableLiveData<List<LibraryItem>> = MutableLiveData()
    val libraryItemList: LiveData<List<LibraryItem>>
        get() = _libraryItemList

    val audioSwitcher = dataStore.switchers.map { list ->
        list.firstOrNull {
            it.name == DataStorePreferences.AUDIO_SWITCHER_NAME
        }
    }

    fun updateCurrentFragmentTitle(title: String) {
        currentFragmentTitle = title
    }

    fun getRuneDataFromDB() = viewModelScope.launch(ioDispatcher) {
        _libraryItemList.postValue(databaseRepository.getLibraryRootItemsList())
    }

    fun getFilteredLibraryList(idList: List<String>) = viewModelScope.launch(ioDispatcher) {
        _libraryItemList.postValue(databaseRepository.getFilteredLibraryItemsList(idList = idList))
    }

    fun updateStateLoad(error: Boolean) {
        _errorLoad.value = error
    }

    companion object {
        const val DEFAULT_TITLE = ""
    }
}
