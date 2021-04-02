package com.test.runar

import android.app.Application
import com.test.runar.controllers.MusicController
import com.test.runar.repository.LanguageRepository
import com.test.runar.repository.SharedDataRepository
import com.test.runar.repository.SharedPreferencesRepository
import com.test.runar.room.AppDB
import com.test.runar.room.DataDB

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