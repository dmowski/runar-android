package com.tnco.runar.feature_audio_fairytailes.presentation.player

import androidx.lifecycle.*
import com.tnco.runar.model.LibraryItemsModel
import com.tnco.runar.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _audioFairyTales = MutableStateFlow<Map<LibraryItemsModel, List<LibraryItemsModel>>>(emptyMap())
    val audioFairyTales = _audioFairyTales.asStateFlow()

    fun getLibraryItems() = viewModelScope.launch(Dispatchers.IO) {
        val libraryItems = databaseRepository.getLibraryItemList()
        _audioFairyTales.update {
            libraryItemsToMap(libraryItems)
        }
    }

    private fun libraryItemsToMap(
        libraryItems: List<LibraryItemsModel>
    ): Map<LibraryItemsModel, List<LibraryItemsModel>> {

        val audioMap = mutableMapOf<LibraryItemsModel, List<LibraryItemsModel>>()

        val headersIds = libraryItems
            .filter { it.type == "root" && it.title == "Скандинавские сказки" }
            .flatMap { it.childs ?: emptyList() }

        libraryItems
            .filter { it.type == "subMenu" && headersIds.contains(it.id) }
            .sortedBy { it.sortOrder }
            .forEach { key ->
                audioMap[key] = libraryItems
                    .filter {
                        key.childs?.contains(it.id) ?: false &&
                            it.audioDuration != null &&
                            it.audioUrl?.isNotEmpty() ?: false
                    }
                    .sortedBy { it.sortOrder }
            }

        return audioMap.filter { it.value.isNotEmpty() }
    }
}
