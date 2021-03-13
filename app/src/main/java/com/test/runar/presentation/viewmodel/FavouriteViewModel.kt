package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.extensions.SingleLiveEvent
import com.test.runar.model.FavUserLayoutModel
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.model.LibraryItemsModel
import com.test.runar.model.UserLayoutModel
import com.test.runar.repository.DatabaseRepository
import com.test.runar.repository.SharedDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                header = item.saveDate.toString(),
                content = item.interpretation,
                imgId = null,
                id = item.id
            )
            correctData.add(correctItem)
        }
        _favData.postValue(correctData)
    }
}