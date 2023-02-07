package com.tnco.runar.ui.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.feature.MusicController
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.repository.backend.BackendRepository
import com.tnco.runar.repository.LanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val musicController: MusicController,
    private val backendRepository: BackendRepository,
    private val languageRepository: LanguageRepository,
    sharedDataRepository: SharedDataRepository
) : ViewModel() {

    private val preferencesRepository = SharedPreferencesRepository.get()

    val fontSize: LiveData<Float> = sharedDataRepository.fontSize
    val musicStatus: MutableLiveData<Boolean> = MutableLiveData(true)
    val onboardingStatus: MutableLiveData<Boolean> = MutableLiveData(true)
    val selectedLanguagePos: MutableLiveData<Int> = MutableLiveData(0)
    private var langOrder = arrayListOf("ru", "en")
    val headerUpdater: MutableLiveData<Boolean> = MutableLiveData(true)

    fun updateLocaleData() {
        when (languageRepository.currentAppLanguage()) {
            "ru" -> selectedLanguagePos.postValue(0)
            else -> selectedLanguagePos.postValue(1)
        }
    }

    fun changeLanguage(pos: Int) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(langOrder[pos]))
        selectedLanguagePos.postValue(pos)
        headerUpdater.postValue(!headerUpdater.value!!)
        CoroutineScope(Dispatchers.IO).launch {
            backendRepository.getLibraryData(langOrder[pos])
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
