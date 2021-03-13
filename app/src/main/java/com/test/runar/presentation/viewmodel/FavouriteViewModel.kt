package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.RunarLogger
import com.test.runar.model.FavUserLayoutModel
import com.test.runar.model.UserLayoutModel
import com.test.runar.repository.DatabaseRepository
import com.test.runar.repository.SharedDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FavouriteViewModel: ViewModel() {
    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    var favList: List<UserLayoutModel> = emptyList()
    var favData = MutableLiveData<List<FavUserLayoutModel>>()
    var haveSelectedItem = MutableLiveData(false)


    fun getUserLayoutsFromDB() {
        CoroutineScope(Dispatchers.IO).launch {
            favList = DatabaseRepository.getUserLayouts()
            getCorrectUserData()
        }
    }
    private fun getCorrectUserData(){
        val correctData = mutableListOf<FavUserLayoutModel>()
        val checkboxMap = mutableMapOf<Int,Boolean>()
        for(item in favList){
            val correctItem = FavUserLayoutModel(
                header = dateCorrection(item.saveDate),
                content = textCorrection(item.interpretation),
                imgId = null,
                id = item.id,
                selected = false
            )
            correctData.add(correctItem)
            checkboxMap[item.id!!] = true
        }
        favData.postValue(correctData)
    }

    fun removeSelectedLayouts(){
        val idsList = mutableListOf<Int>()
        for(item in favData.value!!){
            if(item.selected!=null){
                if(item.selected!!){
                    idsList.add(item.id!!)
                }
            }
        }
        RunarLogger.logDebug(idsList.size.toString())
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseRepository.deleteUserLayoutsByIds(idsList)
            favList = DatabaseRepository.getUserLayouts()
            getCorrectUserData()
        }
    }

    /*хз как иначе заставить компоуз обновить данные, пока решение добавлять и удалять странный объект в список каждый раз, лол*/
    fun changeSelection(id: Int){
        val newData = mutableListOf<FavUserLayoutModel>()
        val oldData = favData.value
        var shitExist = true
        if (oldData != null) {
            for(item in oldData){
                if(item.id==id){
                    item.selected=!(item.selected)!!
                }
                if(item.id==666999){
                    shitExist=false
                }
                else newData.add(item)
            }
        }
        if(shitExist) newData.add(FavUserLayoutModel(null,null,null,666999,null))
        var selectedExist = false
        for(item in newData){
            if(item.selected==true) selectedExist = true
        }
        haveSelectedItem.postValue(selectedExist)
        favData.postValue(newData)
    }

    fun changeAll(state: Boolean){
        val newData = mutableListOf<FavUserLayoutModel>()
        val oldData = favData.value
        var shitExist = true
        if (oldData != null) {
            for(item in oldData){
                item.selected=state
                if(item.id==666999){
                    shitExist=false
                }
                else newData.add(item)
            }
        }
        if(shitExist) newData.add(FavUserLayoutModel(null,null,null,666999,null))
        var selectedExist = false
        for(item in newData){
            if(item.selected==true) selectedExist = true
        }
        haveSelectedItem.postValue(selectedExist)
        favData.postValue(newData)
    }

    private fun textCorrection(text: String?): String{
        var newText =""
        if(text!=null){
            newText = text
                .replace("<bf>","")
                .replace("</bf>","")
                .replace("<br>"," ")
        }
        else newText ="WTF"
        newText = newText.substring(0,52)
        newText+="..."
        return newText
    }

    private fun dateCorrection(time: Long?): String{
        if(time!=null){
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")
            val netDate = Date(time*1000)
            return sdf.format(netDate)
        }
        else return "WTF"
    }
}