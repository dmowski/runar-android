package com.test.runar.presentation.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.runar.controllers.MusicController
import com.test.runar.model.FavUserLayoutModel
import com.test.runar.repository.BackendRepository
import com.test.runar.repository.LanguageRepository
import com.test.runar.repository.SharedDataRepository
import com.test.runar.repository.SharedPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SettingsViewModel: ViewModel() {
    private val preferencesRepository = SharedPreferencesRepository.get()

    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    var musicStatus = MutableLiveData<Boolean>(true)
    var selectedLanguagePos = MutableLiveData(0)
    var langOrder = arrayListOf("ru","en")
    var headerUpdater = MutableLiveData(true)

    fun updateLocaleData(){
        when(Locale.getDefault().language){
            "ru" -> selectedLanguagePos.postValue(0)
            else -> selectedLanguagePos.postValue(1)
        }
    }

    fun changeLanguage(pos: Int, activity: Activity){
        LanguageRepository.changeLanguage(activity,langOrder[pos])
        preferencesRepository.changeSettingsLanguage(langOrder[pos])
        selectedLanguagePos.postValue(pos)
        headerUpdater.postValue(!headerUpdater.value!!)
        CoroutineScope(Dispatchers.IO).launch {
            BackendRepository.getLibraryData(langOrder[pos])
        }
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