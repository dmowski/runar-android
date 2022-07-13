package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.*
import com.tnco.runar.model.RunesItemsModel
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.backend.BackendRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeneratorStartViewModel : ViewModel() {

    var readRunes: LiveData<List<RunesItemsModel>> =
        DatabaseRepository.getRunesGenerator().asLiveData()

    var runesResponse: MutableLiveData<List<RunesItemsModel>> = MutableLiveData()

    fun getRunes() = CoroutineScope(Dispatchers.IO).launch {
        runesResponse.postValue(BackendRepository.getRunes())
    }
}