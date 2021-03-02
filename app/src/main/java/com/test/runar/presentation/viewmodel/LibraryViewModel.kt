package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.R
import com.test.runar.RunarLogger
import com.test.runar.model.LibraryItemsModel
import com.test.runar.repository.SharedDataRepository

class LibraryViewModel : ViewModel() {

    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    val dbList: List<LibraryItemsModel> = listOf(
        LibraryItemsModel(R.drawable.library_item1_pic,"Описание рун","Письменность древних германцев, употреблявшаяся с I—II по XII век","root",1,null),
        LibraryItemsModel(R.drawable.library_item2_pic,"История рун","Первые руны появились в дохристианском мире","root",2,null),
        LibraryItemsModel(R.drawable.library_item3_pic,"Старшая Эдда","Сборник древнеисландских песен о богах и героях мифологии","root",3,null),
        LibraryItemsModel(null,"Скандинавские сказки",null,"simpleMenu",25,1),
        LibraryItemsModel(null,"Песни о героях",null,"simpleMenu",26,1),
        LibraryItemsModel(null,"1","йотунов помню,\n" +
                "до начала рожденных,\n" +
                "кои меня\n" +
                "древле родили,\n" +
                "и девять знаю\n" +
                "земель — все девять\n" +
                "от древа предела\n" +
                "корня земные,","fullText",30,25),
        LibraryItemsModel(null,"2","йотунов помню,\n" +
                "до начала рожденных,\n" +
                "кои меня\n" +
                "древле родили,\n" +
                "и девять знаю\n" +
                "земель — все девять\n" +
                "от древа предела\n" +
                "корня земные,","fullText",40,26))

    var _currentMenu = MutableLiveData<List<LibraryItemsModel>>(emptyList())
    var currentMenu: LiveData<List<LibraryItemsModel>> = _currentMenu
    var currentNav = MutableLiveData<MutableList<Int>>(mutableListOf())
    fun setCurrentMenu(id: Int){
        val newList = mutableListOf<LibraryItemsModel>()
        for(item in dbList){
            if(item.parentId==id) newList.add(item)
        }
        _currentMenu.postValue(newList)
        currentNav.value?.add(id)
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
                    currentNav.value?.removeLast()
                    currentNav.value?.removeLast()
                }
                else{
                    mainMenuDraw()
                }
            }
        }
    }

}