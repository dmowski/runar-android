package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.RunarLogger
import com.test.runar.model.LibraryItemsModel
import com.test.runar.model.NewLibraryItemsModel
import com.test.runar.repository.DatabaseRepository
import com.test.runar.repository.SharedDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {

    val newData: List<NewLibraryItemsModel> = listOf(
        NewLibraryItemsModel(
            childs = listOf("6047e3d0f9cd129f52ae3c7c","6047e3d0f9cd129f52ae3c84","6047e3d2f9cd129f52ae3c89"),
            id = "6047e3cff9cd129f52ae3c7a",
            content = "Письменность древних германцев, употреблявшаяся с I—II по XII век",
            title = "Описание рун",
            imageUrl = "https://lineform.s3.eu-west-2.amazonaws.com/temp_runar/1.png",
            sortOrder = 11,
            type = "root"),
        NewLibraryItemsModel(
            childs = listOf("6047e3d0f9cd129f52ae3c91","6047e3d0f9cd129f52ae3c92","6047e3d2f9cd129f52ae3c93"),
            id = "6047e3cff9cd129f52ae9f7g",
            content = "Первые руны появились в дохристианском мире",
            title = "История рун",
            imageUrl = "https://lineform.s3.eu-west-2.amazonaws.com/temp_runar/2.png",
            sortOrder = 11,
            type = "root"),
        NewLibraryItemsModel(
            childs = listOf("6047e3d0f9cd129f52ae3h6"),
            id = "6047e3d0f9cd129f52ae3c7c",
            content = null,
            title = "Песни о богах",
            imageUrl = null,
            sortOrder = 11,
            type = "subMenu"),
        NewLibraryItemsModel(
            childs = listOf("6047e3d0f9cd129f52ae4h6"),
            id = "6047e3d0f9cd129f52ae3c84",
            content = null,
            title = "Песни о героях",
            imageUrl = null,
            sortOrder = 11,
            type = "subMenu"),
        NewLibraryItemsModel(
            childs = listOf("6047e3d0f9cd129f52ae4h9"),
            id = "6047e3d2f9cd129f52ae3c89",
            content = null,
            title = "Эддические песни",
            imageUrl = null,
            sortOrder = 11,
            type = "subMenu"),
        NewLibraryItemsModel(
            childs = listOf("6047e3d0f9cd129f52a654"),
            id = "6047e3d0f9cd129f52ae3h6",
            content = null,
            title = "test1",
            imageUrl = null,
            sortOrder = 11,
            type = "subMenu"),
        NewLibraryItemsModel(
            childs = listOf(),
            id = "6047e3d0f9cd129f52a654",
            content = null,
            title = "test2",
            imageUrl = null,
            sortOrder = 11,
            type = "subMenu"),
    )

    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    var dbList: List<LibraryItemsModel> = emptyList()
    var currentNav = MutableLiveData<MutableList<Int>>(mutableListOf())
    var lastMenuHeader = MutableLiveData("Библиотека")

    var menuData = MutableLiveData<Pair<List<NewLibraryItemsModel>, MutableList<String>>>(Pair(
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
        RunarLogger.logDebug(menuNavData.toString())
    }

    private fun updateMenuData() {
        val newList = mutableListOf<NewLibraryItemsModel>()
        for (item in newData) {
            if (item.type == "root") newList.add(item)
        }
        menuNavData.clear()
        menuData.postValue(Pair(newList, mutableListOf()))
    }

    fun updateMenuData(id: String){
        val newList = mutableListOf<NewLibraryItemsModel>()
        var selectedItem = NewLibraryItemsModel()
        for(curItem in newData){
            if(curItem.id==id) selectedItem = curItem
        }

        for (item in newData) {
            for(child in selectedItem.childs!!){
                if (child == item.id){
                    newList.add(item)
                }
            }
        }
        menuNavData.add(id)
        /*val newRoute = mutableListOf<String>()
        var routLength = 0
        for (menuId in menuNavData) {
            if (menuId != 8688) {
                for (item in dbList) {
                    if (item.id == menuId) {
                        val routeString = "> " + item.title + "  "
                        newRoute.add(routeString)
                        routLength += routeString.length
                    }
                }
            }
        }
        if (routLength > 45) {
            newRoute.removeLast()
            newRoute.add(">...")
        }*/
        menuData.postValue(Pair(newList, mutableListOf()))
    }

    fun goBackInMenu() {
        RunarLogger.logDebug("back")
        if(menuNavData.size>1){
            menuNavData.removeLast()
            updateMenuData(menuNavData.last())
            menuNavData.removeLast()
        }
        else{
            updateMenuData()
        }
    }

    fun updateLastMenuHeader(header: String) {/*
        var newHeader = header
        var lastId = 8688
        if (menuNavData.size > 0) lastId = menuNavData.last()

        if (lastId == 8688) newHeader = header
        else {
            for (item in dbList) {
                if (item.id == lastId) {
                    if (item.title != null) newHeader = item.title!!
                }
            }
        }
        lastMenuHeader.value = newHeader*/
    }
}