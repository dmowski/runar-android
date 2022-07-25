package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tnco.runar.model.RunesItemsModel
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.GeneratorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneratorStartViewModel @Inject constructor(
    private val generatorRepository: GeneratorRepository,
    databaseRepository: DatabaseRepository
): ViewModel() {

    val readRunes: LiveData<List<RunesItemsModel>> =
        databaseRepository.getRunesGenerator().asLiveData()

    val runesResponse: MutableLiveData<List<RunesItemsModel>> = MutableLiveData()

    fun getRunes() = CoroutineScope(Dispatchers.IO).launch {
        runesResponse.postValue(generatorRepository.getRunes())
    }
}