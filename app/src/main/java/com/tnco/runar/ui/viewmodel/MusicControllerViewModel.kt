package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.tnco.runar.feature.MusicController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MusicControllerViewModel @Inject constructor(
    private val musicController: MusicController
) : ViewModel() {

    fun startMusic() {
        musicController.startMusic()
    }

    fun stopMusic() {
        musicController.stopMusic()
    }

    fun softStopMusic() {
        musicController.softStopMusic()
    }

    fun currentSongPos() = musicController.currentSongPos()

    fun updateSplashStatus(status: Boolean) = musicController.updateSplashStatus(status)

    fun updateMainStatus(status: Boolean) = musicController.updateMainStatus(status)

    fun updateOnboardingStatus(status: Boolean) = musicController.updateOnboardingStatus(status)
}
