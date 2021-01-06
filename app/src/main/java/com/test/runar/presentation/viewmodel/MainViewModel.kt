package com.test.runar.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.repository.LayoutDescriptionRepository

class MainViewModel : ViewModel() {
    var selectedFragment = MutableLiveData<Fragment>(null)
    var selectedLayout = MutableLiveData<LayoutDescriptionModel>(null)
    var showStatus = MutableLiveData<Boolean>(true)

    fun notShowSelectedLayout(context: Context,id:Int){
        LayoutDescriptionRepository.notShow(context,id)
    }
    fun getLayoutDescription(context: Context,id: Int){
        selectedLayout.postValue(LayoutDescriptionRepository.getLayoutDetails(context,id))
    }
    fun changeFragment(newFragment: Fragment){
        selectedFragment.value = newFragment
    }
    fun clearLayoutData(){
        selectedLayout.postValue(null)
    }

    fun descriptionCheck(context: Context,id:Int) :Boolean{
       // showStatus.postValue(LayoutDescriptionRepository.getShowStatus(context,id))
        return LayoutDescriptionRepository.getShowStatus(context,id)
    }
}