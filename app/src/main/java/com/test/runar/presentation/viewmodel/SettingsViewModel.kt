package com.test.runar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.controllers.MusicController
import com.test.runar.model.FavUserLayoutModel
import com.test.runar.repository.SharedDataRepository
import com.test.runar.repository.SharedPreferencesRepository

class SettingsViewModel: ViewModel() {
    private val preferencesRepository = SharedPreferencesRepository.get()

    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    var musicStatus = MutableLiveData<Boolean>(true)
    var selectedLanguagePos = MutableLiveData(0)

    fun changeLanguage(pos: Int){
        selectedLanguagePos.postValue(pos)
    }

    fun updateMusicStatus(){
        val currentMusicStatus = preferencesRepository.settingsMusic
        val state = currentMusicStatus==1
        musicStatus.postValue(state)
    }

    fun changeMusicStatus(state: Boolean){
        if(state){
            preferencesRepository.changeSettingsMusic(1)
            MusicController.startMusic()
            updateMusicStatus()
        }
        else{
            preferencesRepository.changeSettingsMusic(0)
            MusicController.stopMusic()
            updateMusicStatus()
        }
    }
}