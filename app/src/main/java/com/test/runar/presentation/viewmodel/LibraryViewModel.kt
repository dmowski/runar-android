package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.R
import com.test.runar.RunarLogger
import com.test.runar.extensions.SingleLiveEvent
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.model.LibraryItemsModel
import com.test.runar.repository.DatabaseRepository
import com.test.runar.repository.SharedDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {

    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    val dbList: List<LibraryItemsModel> = listOf(
        LibraryItemsModel(R.drawable.library_item1_pic,"Описание рун","Письменность древних германцев, употреблявшаяся с I—II по XII век",1,1,null,2),
        LibraryItemsModel(R.drawable.library_item2_pic,"История рун","Первые руны появились в дохристианском мире",1,1,null,8),
        LibraryItemsModel(R.drawable.library_item3_pic,"Старшая Эдда","Сборник древнеисландских песен о богах и героях мифологии",1,1,null,8),
        LibraryItemsModel(R.drawable.library_item4_pic,"Младшая Эдда","Прозаический трактат о поэтике скальдов",1,1,null,8),
        LibraryItemsModel(R.drawable.library_item5_pic,"Скандинавские сказки","Сказки стран Норвегии, Дании, Швеции, Финляндии и Исландии",1,1,null,8),
        LibraryItemsModel(null,"Скандинавские сказки",null,2,2,null,3),
        LibraryItemsModel(null,"Песни о героях",null,2,2,null,4),
        LibraryItemsModel(null,null,"йотунов помню,\n" +
                "до начала рожденных,\n" +
                "кои меня\n" +
                "древле родили,\n" +
                "и девять знаю\n" +
                "земель — все девять\n" +
                "от древа предела\n" +
                "корня земные,",3,3,1,8),
        LibraryItemsModel(null,null,"йотунов помню,\n" +
                "до начала рожденных,\n" +
                "кои меня\n" +
                "древле родили,\n" +
                "и девять знаю\n" +
                "земель — все девять\n" +
                "от древа предела\n" +
                "корня земные,",3,4,2,8))

    var _currentMenu = MutableLiveData<List<LibraryItemsModel>>(emptyList())
    var currentMenu: LiveData<List<LibraryItemsModel>> = _currentMenu
    var currentNav = MutableLiveData<MutableList<Int>>(mutableListOf())
    fun setCurrentMenu(menuId: Int){
        var newList = mutableListOf<LibraryItemsModel>()
        for(item in dbList){
            if(item.menuId==menuId) newList.add(item)
        }
        _currentMenu.postValue(newList)
    }
    fun firstMenuDraw(){
        if (currentMenu.value?.size==0) {
            setCurrentMenu(1)
            clearNavAction()
            currentNav.value?.add(1)
        }
        test()
    }
    fun addNavAction(menuId: Int){
        currentNav.value?.add(menuId)
    }
    fun removeNavAction(){
        currentNav.value?.removeLast()
    }
    fun clearNavAction(){
        currentNav.value?.clear()
    }
    fun test(){
        RunarLogger.logDebug(currentNav.value.toString())
    }
    fun goBackInMenu(){
        if(currentNav.value?.last()!=1){
            removeNavAction()
            setCurrentMenu(currentNav.value?.last()!!)
        }
    }

}