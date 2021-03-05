package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.R
import com.test.runar.RunarLogger
import com.test.runar.model.LibraryItemsModel
import com.test.runar.repository.DatabaseRepository
import com.test.runar.repository.SharedDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {

    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    var dbList: List<LibraryItemsModel> = emptyList()
    var _currentMenu = MutableLiveData<List<LibraryItemsModel>>(emptyList())
    var currentMenu: LiveData<List<LibraryItemsModel>> = _currentMenu
    var currentNav = MutableLiveData<MutableList<Int>>(mutableListOf())

    var currentNavRoute = MutableLiveData<MutableList<String>>(mutableListOf())

    var lastMenuHeader = MutableLiveData<String>("Библиотека")

    fun getRuneDataFromDB() {
        CoroutineScope(Dispatchers.IO).launch {
            dbList = DatabaseRepository.getLibraryItemList()
        }
    }


    fun setCurrentMenu(id: Int){
        val newList = mutableListOf<LibraryItemsModel>()
        for(item in dbList){
            if(item.parentId==id) newList.add(item)
        }
        _currentMenu.postValue(newList)
        currentNav.value?.add(id)
    }

    fun returnAndSetCurrentMenu(id: Int){
        val newList = mutableListOf<LibraryItemsModel>()
        for(item in dbList){
            if(item.parentId==id) newList.add(item)
        }
        _currentMenu.postValue(newList)
    }
    fun firstMenuDraw(){
        if (currentMenu.value?.size==0) {
            mainMenuDraw()
        }
        test()
    }

    fun mainMenuDraw(){
        val newList = mutableListOf<LibraryItemsModel>()
        for(item in dbList){
            if(item.typeView=="root") newList.add(item)
        }
        _currentMenu.postValue(newList)
        clearNavAction()
        currentNav.value?.add(8688)
    }
    fun clearNavAction(){
        currentNav.value?.clear()
    }
    fun test(){
        RunarLogger.logDebug(currentNav.value.toString())
    }
    fun goBackInMenu(){
        for(item in dbList){
            if(item.id==currentNav.value?.last()) {
                if(item.parentId!=null){
                    returnAndSetCurrentMenu(item.parentId!!)
                    currentNav.value?.removeLast()
                }
                else{
                    mainMenuDraw()
                }
                return
            }
        }
    }

    fun updateCurrentNavRoute(){
        val newRoute = mutableListOf<String>()
        var routLength =0
        for(id in currentNav.value!!){
            if(id!=8688){
                for(item in dbList){
                    if(item.id==id){
                        val routeString = "> "+item.title+"  "
                        newRoute.add(routeString)
                        routLength+=routeString.length
                    }
                }
            }
        }
        if(routLength>45) {
            newRoute.removeLast()
            newRoute.add(">...")
        }
        currentNavRoute.postValue(newRoute)
    }
    fun updateLastMenuHeader(){
        var newHeader ="Библиотека"
        var lastId = 8688
        if(currentNav.value!!.size>0) lastId = currentNav.value!!.last()

        if(lastId==8688) newHeader ="Библиотека"
        else{
            for(item in dbList){
                if(item.id==lastId){
                    if(item.title!=null) newHeader = item.title!!
                }
            }
        }
        lastMenuHeader.value = newHeader
    }

}