package com.test.runar.presentation.viewmodel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.repository.LayoutDescriptionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var selectedLayout = MutableLiveData<LayoutDescriptionModel>(null)
    var showStatus = MutableLiveData<Int>(3)

    fun notShowSelectedLayout(context: Context,id:Int){
        CoroutineScope(IO).launch {
            LayoutDescriptionRepository.notShow(context,id)
        }
    }
    fun getLayoutDescription(context: Context,id: Int){
        CoroutineScope(IO).launch {
            selectedLayout.postValue(LayoutDescriptionRepository.getLayoutDetails(context,id))
        }
    }
    fun clearLayoutData(){
        selectedLayout.postValue(null)
    }

    fun descriptionCheck(context: Context,id:Int){
        CoroutineScope(IO).launch {
            showStatus.postValue(LayoutDescriptionRepository.getShowStatus(context,id))
        }
    }
    fun clearShowStatus(){
        showStatus.postValue(3)
    }
}