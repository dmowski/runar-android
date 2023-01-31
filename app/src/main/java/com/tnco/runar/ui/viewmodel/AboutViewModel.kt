package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.repository.SharedDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    sharedDataRepository: SharedDataRepository
) : ViewModel() {
    val fontSize: LiveData<Float> = MutableLiveData(sharedDataRepository.fontSize)
}
