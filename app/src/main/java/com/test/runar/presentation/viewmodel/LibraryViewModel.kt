package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.model.LibraryItemsModel
import com.test.runar.repository.DatabaseRepository
import com.test.runar.repository.SharedDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {
    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    var dbList: List<LibraryItemsModel> = emptyList()
    var lastMenuHeader = MutableLiveData("Библиотека")

    var menuData = MutableLiveData<Pair<List<LibraryItemsModel>, MutableList<String>>>(Pair(
        emptyList(), mutableListOf()))

    var menuNavData = mutableListOf<String>()

    fun getRuneDataFromDB() {
        CoroutineScope(Dispatchers.IO).launch {
            dbList = DatabaseRepository.getLibraryItemList()
        }
    }

    fun firstMenuDataCheck() {
        if (menuData.value?.first?.size==0) {
            updateMenuData()
        }
    }

    private fun updateMenuData() {
        val newList = mutableListOf<LibraryItemsModel>()
        for (item in dbList) {
            if (item.type == "root") newList.add(item)
        }
        menuNavData.clear()
        menuData.postValue(Pair(newList, mutableListOf()))
    }

    fun updateMenuData(id: String){
        val newList = mutableListOf<LibraryItemsModel>()
        var selectedItem = LibraryItemsModel()
        for(curItem in dbList){
            if(curItem.id==id) selectedItem = curItem
        }

        for (item in dbList) {
            for(child in selectedItem.childs!!){
                if (child == item.id){
                    newList.add(item)
                }
            }
        }
        menuNavData.add(id)

        val newRoute = mutableListOf<String>()
        var routLength = 0
        for (menuId in menuNavData) {
            for (item in dbList) {
                if (item.id == menuId) {
                    val routeString = "> " + item.title + "  "
                    newRoute.add(routeString)
                    routLength += routeString.length
                }
            }
        }
        if (routLength > 45) {
            newRoute.removeLast()
            newRoute.add(">...")
        }
        menuData.postValue(Pair(newList, newRoute))
    }

    fun goBackInMenu() {
        if(menuNavData.size>1){
            menuNavData.removeLast()
            val last = menuNavData.last()
            menuNavData.removeLast()
            updateMenuData(last)
        }
        else{
            updateMenuData()
        }
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
}