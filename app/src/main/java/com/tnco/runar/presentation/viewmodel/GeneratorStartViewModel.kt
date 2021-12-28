package com.tnco.runar.presentation.viewmodel

import androidx.lifecycle.*
import com.tnco.runar.model.RunesItemsModel
import com.tnco.runar.repository.BackendRepository
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.retrofit.RunesResponse
import kotlinx.coroutines.launch

class GeneratorStartViewModel:ViewModel() {

    var readRunes: LiveData<List<RunesItemsModel>> = DatabaseRepository.getRunesGenerator().asLiveData()

    var runesResponse: MutableLiveData<List<RunesItemsModel>> = MutableLiveData()

    fun getRunes() = viewModelScope.launch {
        runesResponse.value = BackendRepository.getRunes()
    }
}