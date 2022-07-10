package com.tnco.runar

import android.app.Application
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.data.local.AppDB
import com.tnco.runar.data.local.DataDB
import com.tnco.runar.feature.MusicController
import com.tnco.runar.repository.LanguageRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.SharedPreferencesRepository

class App : Application() {
    override fun onCreate() {
        SharedPreferencesRepository.init(this)
        LanguageRepository.setInitialSettingsLanguage(this)
        AppDB.init(this)
        DataDB.init(this)
        SharedDataRepository.init(this)
        MusicController.init(this)
        AnalyticsHelper.init()
        super.onCreate()
    }
}