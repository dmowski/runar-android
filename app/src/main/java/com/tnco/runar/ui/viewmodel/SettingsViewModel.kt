package com.tnco.runar.ui.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnco.runar.di.annotations.IoDispatcher
import com.tnco.runar.feature.MusicController
import com.tnco.runar.repository.LanguageRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.repository.backend.BackendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val musicController: MusicController,
    private val backendRepository: BackendRepository,
    private val languageRepository: LanguageRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    sharedDataRepository: SharedDataRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

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
        viewModelScope.launch(ioDispatcher) {
            backendRepository.getLibraryData(langOrder[pos])
        }
    }

    fun updateMusicStatus() {
        val currentMusicStatus = sharedPreferencesRepository.settingsMusic
        val state = currentMusicStatus == 1
        musicStatus.postValue(state)
    }

    fun changeMusicStatus(state: Boolean) {
        if (state) {
            sharedPreferencesRepository.changeSettingsMusic(1)
            musicController.startMusic()
            updateMusicStatus()
        } else {
            sharedPreferencesRepository.changeSettingsMusic(0)
            musicController.stopMusic()
            updateMusicStatus()
        }
    }

    fun updateOnboardingStatus() {
        val currentOnboardingStatus = sharedPreferencesRepository.settingsOnboarding
        val state = currentOnboardingStatus == 1
        onboardingStatus.postValue(state)
    }

    fun changeOnboardingStatus(state: Boolean) {
        if (state) {
            sharedPreferencesRepository.changeSettingsOnboarding(1)
            updateOnboardingStatus()
        } else {
            sharedPreferencesRepository.changeSettingsOnboarding(0)
            updateOnboardingStatus()
        }
    }
}
