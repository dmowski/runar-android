package com.tnco.runar.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnco.runar.model.RunesItemsModel
import com.tnco.runar.repository.BackendRepository
import com.tnco.runar.retrofit.RunesResponse
import kotlinx.coroutines.launch

class GeneratorStartViewModel:ViewModel() {

    var runesResponse: MutableLiveData<List<RunesItemsModel>> = MutableLiveData()

    fun getRunes() = viewModelScope.launch {
        runesResponse.value = BackendRepository.getRunes()
    }
}