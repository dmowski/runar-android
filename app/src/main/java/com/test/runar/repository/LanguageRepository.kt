package com.test.runar.repository

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import com.test.runar.room.AppDB
import com.test.runar.ui.activity.MainActivity
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
        AppDB.init(activity)
        DatabaseRepository.reinit()
        SharedDataRepository.init(activity)

        (activity as MainActivity).reshowBar()
    }
    fun setSettingsLanguage(activity: Activity){
        val prefRep = SharedPreferencesRepository.get()
        val locale = Locale(prefRep.language)
        Locale.setDefault(locale)
        val config: Configuration? = activity.baseContext?.resources?.configuration
        config?.locale = locale
        activity.baseContext?.resources?.updateConfiguration(
            config,
            activity.baseContext?.resources?.displayMetrics
        )
        AppDB.init(activity)
        DatabaseRepository.reinit()
        SharedDataRepository.init(activity)
    }
    fun setInitialSettingsLanguage(context: Context){
        val prefRep = SharedPreferencesRepository.get()
        val locale = Locale(prefRep.language)
        Locale.setDefault(locale)
        val config: Configuration? = context.resources?.configuration
        config?.locale = locale
        context.resources?.updateConfiguration(
            config,
            context.resources?.displayMetrics
        )
    }
}