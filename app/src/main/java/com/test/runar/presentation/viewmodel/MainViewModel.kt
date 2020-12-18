package com.test.runar.presentation.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var selectedFragment = MutableLiveData<Fragment>(null)
    fun changeFragment(newFragment: Fragment){
        selectedFragment.value = newFragment
    }
}