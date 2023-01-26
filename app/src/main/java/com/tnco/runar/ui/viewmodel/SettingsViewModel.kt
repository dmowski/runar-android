package com.tnco.runar.ui.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.feature.MusicController
import com.tnco.runar.repository.LanguageRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.repository.backend.BackendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val musicController: MusicController
) : ViewModel() {

    private val preferencesRepository = SharedPreferencesRepository.get()

    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    val musicStatus: MutableLiveData<Boolean> = MutableLiveData(true)
    val onboardingStatus: MutableLiveData<Boolean> = MutableLiveData(true)
    val selectedLanguagePos: MutableLiveData<Int> = MutableLiveData(0)
    private var langOrder = arrayListOf("ru", "en")
    val headerUpdater: MutableLiveData<Boolean> = MutableLiveData(true)

    fun updateLocaleData() {
        when (Locale.getDefault().language) {
            "ru" -> selectedLanguagePos.postValue(0)
            else -> selectedLanguagePos.postValue(1)
        }
    }

    fun changeLanguage(pos: Int, activity: Activity) {
        LanguageRepository.changeLanguage(activity, langOrder[pos])
        preferencesRepository.changeSettingsLanguage(langOrder[pos])
        selectedLanguagePos.postValue(pos)
        headerUpdater.postValue(!headerUpdater.value!!)
        CoroutineScope(Dispatchers.IO).launch {
            BackendRepository.getLibraryData(langOrder[pos])
        }
    }

    fun updateMusicStatus() {
        val currentMusicStatus = preferencesRepository.settingsMusic
        val state = currentMusicStatus == 1
        musicStatus.postValue(state)
    }

    fun changeMusicStatus(state: Boolean) {
        if (state) {
            preferencesRepository.changeSettingsMusic(1)
            musicController.startMusic()
            updateMusicStatus()
        } else {
            preferencesRepository.changeSettingsMusic(0)
            musicController.stopMusic()
            updateMusicStatus()
        }
    }

    fun updateOnboardingStatus() {
        val currentOnboardingStatus = preferencesRepository.settingsOnboarding
        val state = currentOnboardingStatus == 1
        onboardingStatus.postValue(state)
    }

    fun changeOnboardingStatus(state: Boolean) {
        if (state) {
            preferencesRepository.changeSettingsOnboarding(1)
            updateOnboardingStatus()
        } else {
            preferencesRepository.changeSettingsOnboarding(0)
            updateOnboardingStatus()
        }
    }
}
