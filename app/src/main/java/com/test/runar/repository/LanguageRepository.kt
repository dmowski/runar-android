package com.test.runar.repository

import android.app.Activity
import android.content.res.Configuration
import java.util.*

object LanguageRepository {
    fun changeLanguage(activity: Activity,language: String){
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config: Configuration? = activity.baseContext?.resources?.configuration
        config?.locale = locale
        activity.baseContext?.resources?.updateConfiguration(
            config,
            activity.baseContext?.resources?.displayMetrics
        )
    }
}