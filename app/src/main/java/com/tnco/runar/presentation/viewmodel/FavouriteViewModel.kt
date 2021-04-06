package com.tnco.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.model.*
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FavouriteViewModel: ViewModel() {
    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    private var favList: List<UserLayoutModel> = emptyList()
    private var runesData: List<RuneDescriptionModel> = emptyList()
    private var layoutsData: List<LayoutDescriptionModel> = emptyList()
    private var twoRunesInters: List<TwoRunesInterModel> = emptyList()
    var favData = MutableLiveData<List<FavUserLayoutModel>>()
    var haveSelectedItem = MutableLiveData(false)

    fun getUserLayoutsFromDB() {
        CoroutineScope(Dispatchers.IO).launch {
            favList = DatabaseRepository.getUserLayouts().asReversed().take(500)
            runesData = DatabaseRepository.getRunesList()
            layoutsData = DatabaseRepository.getAllLayouts()
            twoRunesInters = DatabaseRepository.getAllTwoRunesInter()
            getCorrectUserData()
        }
    }
    private fun getCorrectUserData(){
        val correctData = mutableListOf<FavUserLayoutModel>()
        val checkboxMap = mutableMapOf<Int,Boolean>()
        for(item in favList){
            val data = intArrayOf(item.layoutId!!,item.slot1!!,item.slot2!!,item.slot3!!,item.slot4!!,item.slot5!!,item.slot6!!,item.slot7!!)
            val inter = getInterpretation(item.layoutId!!,data = data)
            val correctItem = FavUserLayoutModel(
                header = getHeader(item.layoutId!!),
                content = textCorrection(inter),
                time = dateCorrection(item.saveDate),
                id = item.id,
                selected = false,
                layoutId = item.layoutId,
                userData = data,
                affirmId = item.affirmId
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
        if(shitExist) newData.add(FavUserLayoutModel(null,null,null,666999,null,null,null,null))
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
        if(shitExist) newData.add(FavUserLayoutModel(null,null,null,666999,null,null,null,null))
        var selectedExist = false
        for(item in newData){
            if(item.selected==true) selectedExist = true
        }
        haveSelectedItem.postValue(selectedExist)
        favData.postValue(newData)
    }

    private fun textCorrection(text: String?): String{
        var newText =""
        var complete = false
        newText = text ?: "WTF"
        val maxStrSize = 37
        var curInd =newText.indexOf(" ")
        var prevInd =newText.indexOf(" ")
        while(curInd<maxStrSize&&curInd!=-1){
            prevInd = curInd
            curInd = newText.indexOf(" ",prevInd+1)
        }
        var end = 36+prevInd
        if(end>newText.length) {
            if(end>newText.length+3) complete = true
            end = newText.length
        }
        newText = newText.substring(0,end)
        if(!complete) newText+="..."
        return newText
    }

    private fun dateCorrection(time: Long?): String{
        if(time!=null){
            val sdf = SimpleDateFormat("dd.MM.yyyy   HH:mm")
            val netDate = Date(time*1000)
            return sdf.format(netDate)
        }
        else return "WTF"
    }

    private fun getHeader(layoutId: Int): String{
        var header =""
        for(item in layoutsData){
            if(item.layoutId==layoutId) header=item.layoutName!!
        }
        return header
    }

    private fun getInterpretation(layoutId: Int, data: IntArray): String {
        var result= ""
        when (layoutId) {
            1 -> result = getFullDescriptionForRune(data[1]) + "."
            2 -> {
                val index = data[1] * 100 + data[2]
                val inter = getTwoRunesInter(index)
                val res = String.format(getSelectedLayoutInter(layoutId),inter)
                result = res
            }
            3 -> result = String.format(getSelectedLayoutInter(layoutId),
                getMeaningForRune(data[1]),getMeaningForRune(data[2]),getMeaningForRune(data[3]))

            4 -> result = String.format(getSelectedLayoutInter(layoutId),
                getMeaningForRune(data[1]),getMeaningForRune(data[2]),getMeaningForRune(data[3]),getMeaningForRune(data[4]))
            5 -> result = String.format(getSelectedLayoutInter(layoutId),
                getMeaningForRune(data[1]),getMeaningForRune(data[2]),getMeaningForRune(data[4]))
            6 -> result = String.format(getSelectedLayoutInter(layoutId),
                getMeaningForRune(data[1]),getMeaningForRune(data[2]),getMeaningForRune(data[3]),getMeaningForRune(data[5]),getMeaningForRune(data[4]))
            7 -> result = String.format(getSelectedLayoutInter(layoutId),
                getMeaningForRune(data[2]),getMeaningForRune(data[1]),getMeaningForRune(data[4]),getMeaningForRune(data[3]),getMeaningForRune(data[5]),getMeaningForRune(data[6]))
            8 -> result = String.format(getSelectedLayoutInter(layoutId),
                getMeaningForRune(data[1]),getMeaningForRune(data[2]),getMeaningForRune(data[3]),getMeaningForRune(data[4]),getMeaningForRune(data[5]),getMeaningForRune(data[6]),getMeaningForRune(data[7]))
        }
        return result
    }

    private fun getFullDescriptionForRune(id: Int): String {
        for (rune in runesData) {
            if (rune.runeId == id) {
                return rune.fullDescription!!
            }
        }
        return ""
    }

    private fun getSelectedLayoutInter(id: Int):String{
        var res =""
        for(item in layoutsData){
            if(item.layoutId==id) res = item.interpretation!!
        }
        return res
    }

    private fun getTwoRunesInter(id: Int): String{
        var res =" "
        for(inter in twoRunesInters){
            if(inter.id==id) res = inter.text!!
        }
        return res
    }

    private fun getMeaningForRune(id: Int): String {
        for (rune in runesData) {
            if (rune.runeId == id) {
                return rune.meaning!!
            }
        }
        return ""
    }
}