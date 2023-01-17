package com.tnco.runar.repository

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageRepository @Inject constructor(
    private val preferencesRepository: SharedPreferencesRepository,
    private val sharedDataRepository: SharedDataRepository,
    private val databaseRepository: DatabaseRepository
) {
    fun changeLanguageAndUpdateRepo(activity: Activity, language: String) {
        changeLanguage(activity, language)
        databaseRepository.reinit()
        sharedDataRepository.init(activity)
    }

    fun setInitialSettingsLanguage(context: Context) {
        val locale = Locale(preferencesRepository.language)
        Locale.setDefault(locale)
        val config: Configuration? = context.resources?.configuration
        config?.locale = locale
        context.resources?.updateConfiguration(
            config,
            context.resources?.displayMetrics
        )
    }

    private fun changeLanguage(activityContext: Activity, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config: Configuration = activityContext.resources.configuration
        config.setLocales(LocaleList(locale))
        activityContext.createConfigurationContext(config)
    }
}
