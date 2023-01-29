package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.*
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.model.LibraryItemsModel
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    private val databaseRepository: DatabaseRepository,
    val analyticsHelper: AnalyticsHelper
) : ViewModel() {

    private val singleThread = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    internal var dbList: List<LibraryItemsModel> = emptyList()
    var lastMenuHeader = MutableLiveData("")

    private val _errorLoad = MutableLiveData<Boolean>()
    val errorLoad: LiveData<Boolean>
        get() = _errorLoad

    var scrollPositionHistory = MutableLiveData(mutableListOf(0))

    var menuData = MutableLiveData<List<LibraryItemsModel>>(emptyList())

    val isOnline = networkMonitor.isConnected.asLiveData()

    private var menuNavData = mutableListOf<String>()

    fun getRuneDataFromDB() {
        CoroutineScope(singleThread).launch {
            dbList = databaseRepository.getLibraryItemList()
        }
    }

    fun firstMenuDataCheck() {
        CoroutineScope(singleThread).launch {
            if (menuData.value?.size == 0) {
                updateMenuData()
            }
        }
    }
    fun updateStateLoad(error: Boolean) {
        _errorLoad.value = error
    }

    private fun updateMenuData() {
        val newList = mutableListOf<LibraryItemsModel>()
        for (item in dbList) {
            if (item.type == "root") newList.add(item)
        }
        menuNavData.clear()
        newList.sortBy { it.sortOrder }
        menuData.postValue(newList)
    }

    fun updateMenuData(id: String) {
        val newList = mutableListOf<LibraryItemsModel>()
        var selectedItem = LibraryItemsModel()
        for (curItem in dbList) {
            if (curItem.id == id) selectedItem = curItem
        }

        for (item in dbList) {
            for (child in selectedItem.childs!!) {
                if (child == item.id) {
                    newList.add(item)
                }
            }
        }

        newList.sortBy { it.sortOrder }
        if (newList.size == 0) {
            updateMenuData()
            return
        }
        menuNavData.add(id)
        menuData.postValue(newList)
    }

    fun goBackInMenu() {
        if (menuNavData.size > 1) {
            menuNavData.removeLast()
            val last = menuNavData.last()
            menuNavData.removeLast()
            updateMenuData(last)
        } else {
            updateMenuData()
        }
        addScrollPositionHistory(9999)
    }

    fun updateLastMenuHeader(header: String) {
        var newHeader = header
        var lastId: String? = null
        if (menuNavData.size > 0) lastId = menuNavData.last()
        if (lastId == null) newHeader = header
        else {
            for (item in dbList) {
                if (item.id == lastId) {
                    if (item.title != null) newHeader = item.title!!
                }
            }
        }
        lastMenuHeader.value = newHeader
    }

    fun addScrollPositionHistory(pos: Int) {
        scrollPositionHistory.value?.add(pos)
    }

    fun removeLastScrollPositionHistory() {
        if (scrollPositionHistory.value?.size!! > 1) {
            scrollPositionHistory.value?.removeLast()
        }
    }
}
