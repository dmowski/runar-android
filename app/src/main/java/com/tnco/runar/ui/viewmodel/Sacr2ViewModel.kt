package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.tnco.runar.repository.SharedDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Sacr2ViewModel @Inject constructor(
    sharedDataRepository: SharedDataRepository
) : ViewModel() {

    val fontSize = sharedDataRepository.fontSize
}
