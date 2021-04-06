package com.tnco.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.repository.SharedDataRepository

class AboutViewModel: ViewModel() {
    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
}