package com.test.runar.presentation.viewmodel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.repository.LayoutDescriptionRepository

class MainViewModel : ViewModel() {
    var selectedLayout = MutableLiveData<LayoutDescriptionModel>(null)

    fun notShowSelectedLayout(context: Context,id:Int){
        LayoutDescriptionRepository.notShow(context,id)
    }
    fun getLayoutDescription(context: Context,id: Int){
        selectedLayout.postValue(LayoutDescriptionRepository.getLayoutDetails(context,id))
    }
    fun clearLayoutData(){
        selectedLayout.postValue(null)
    }

    fun descriptionCheck(context: Context,id:Int) :Boolean{
        return LayoutDescriptionRepository.getShowStatus(context,id)
    }
}