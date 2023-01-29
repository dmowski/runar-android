package com.tnco.runar.repository

import android.app.Activity
import android.content.res.Configuration
import com.tnco.runar.data.local.AppDB
import java.util.*
import javax.inject.Inject

class LanguageRepository @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {

    fun changeLanguage(activity: Activity, language: String) { // TODO get rid of activity of whole repository?
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config: Configuration? = activity.baseContext?.resources?.configuration
        config?.locale = locale
        activity.baseContext?.resources?.updateConfiguration(
            config,
            activity.baseContext?.resources?.displayMetrics
        )
        AppDB.init(activity)
        databaseRepository.reinit()
        SharedDataRepository.init(activity)
    }
}
