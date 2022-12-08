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
import com.tnco.runar.ui.activity.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: SharedPreferencesRepository,
    private val backendRepository: BackendRepository,
    private val languageRepository: LanguageRepository,
    private val musicController: MusicController,
    sharedDataRepository: SharedDataRepository
) : ViewModel() {

    val fontSize: LiveData<Float> = MutableLiveData(sharedDataRepository.fontSize)
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
        languageRepository.changeLanguageAndUpdateRepo(activity, langOrder[pos])
        preferencesRepository.changeSettingsLanguage(langOrder[pos])
        (activity as MainActivity).reshowBar()
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
