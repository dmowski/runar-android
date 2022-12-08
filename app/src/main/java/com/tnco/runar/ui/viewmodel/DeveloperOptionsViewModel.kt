package com.tnco.runar.ui.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.SharedPreferencesRepository

class DeveloperOptionsViewModel : ViewModel() {

    private val preferencesRepository = SharedPreferencesRepository.get()
    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)

    private var _devSwitcherStates: MutableMap<String, Boolean> = switcherStatesFromRepository()
    val devSwitcherStates: Map<String, Boolean>
        get() = _devSwitcherStates

    fun putSwitcherState(key: String, state: Boolean) {
        _devSwitcherStates[key] = state
        preferencesRepository.putSwitcherState(key, state)
    }

    private fun switcherStatesFromRepository() = SnapshotStateMap<String, Boolean>().apply {
        val states = preferencesRepository.switcherStates()
        states.forEach {
            put(it.key, it.value)
        }
    }
}
