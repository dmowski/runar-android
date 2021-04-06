package com.tnco.runar

import android.app.Application
import com.tnco.runar.controllers.MusicController
import com.tnco.runar.repository.LanguageRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.room.AppDB
import com.tnco.runar.room.DataDB

class App : Application() {
    override fun onCreate() {
        SharedPreferencesRepository.init(this)
        LanguageRepository.setInitialSettingsLanguage(this)
        AppDB.init(this)
        DataDB.init(this)
        SharedDataRepository.init(this)
        MusicController.init(this)
        super.onCreate()
    }
}