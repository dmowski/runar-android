package com.tnco.runar

import android.app.Application
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.data.local.AppDB
import com.tnco.runar.repository.LanguageRepository
import com.tnco.runar.repository.SharedDataRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var languageRepository: LanguageRepository

    @Inject
    lateinit var sharedDataRepository: SharedDataRepository

    override fun onCreate() {
        AppDB.init(this)
        super.onCreate()
        sharedDataRepository.init(this)
        languageRepository.setInitialSettingsLanguage(this)
        AnalyticsHelper.init()
    }
}
