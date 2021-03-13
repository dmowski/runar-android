package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private var _favData = MutableLiveData<List<FavUserLayoutModel>>()

    val favData: LiveData<List<FavUserLayoutModel>> = _favData


    fun getUserLayoutsFromDB() {
        CoroutineScope(Dispatchers.IO).launch {
            favList = DatabaseRepository.getUserLayouts()
            getCorrectUserData()
        }
    }
    private fun getCorrectUserData(){
        val correctData = mutableListOf<FavUserLayoutModel>()
        for(item in favList){
            val correctItem = FavUserLayoutModel(
                header = dateCorrection(item.saveDate),
                content = textCorrection(item.interpretation),
                imgId = null,
                id = item.id
            )
            correctData.add(correctItem)
        }
        _favData.postValue(correctData)
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