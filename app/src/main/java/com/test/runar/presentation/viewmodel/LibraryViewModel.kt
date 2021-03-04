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
        RunarLogger.logDebug("yep")
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
                    setCurrentMenu(item.parentId!!)
                    RunarLogger.logDebug("go back, please")
                    currentNav.value?.removeLast()
                    currentNav.value?.removeLast()
                }
                else{
                    mainMenuDraw()
                }
            }
        }
    }

    fun updateCurrentNavRoute(){
        val newRoute = mutableListOf<String>()
        for(id in currentNav.value!!){
            if(id!=8688){
                for(item in dbList){
                    if(item.id==id){
                        newRoute.add("> "+item.title+"  ")
                    }
                }
            }
        }
        if(newRoute.size>2) {
            newRoute.removeLast()
            newRoute.add(">...")
        }
        currentNavRoute.postValue(newRoute)
    }

}