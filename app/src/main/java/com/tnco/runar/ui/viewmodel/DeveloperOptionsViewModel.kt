package com.tnco.runar.ui.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.repository.SharedDataRepository

class DeveloperOptionsViewModel : ViewModel() {

    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)

    private val _devSwitcherStates: MutableMap<String, Boolean> = mutableStateMapOf()
    val devSwitcherStates: Map<String, Boolean>
        get() = _devSwitcherStates

    fun putSwitcherState(name: String, state: Boolean) {
        _devSwitcherStates[name] = state
    }
}