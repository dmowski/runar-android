package com.test.runar.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.util.*

class SharedPreferencesRepository private constructor(context: Context) {
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    val userId: String
    var settingsMusic: Int
    var language: String

    init {
        if (preferences.contains("Id")) {
            userId = preferences.getString("Id", "").toString()
        } else {
            val editor = preferences.edit()
            userId = UUID.randomUUID().toString()
            editor.putString("Id", userId)
            editor.apply()
        }

        if (preferences.contains("Settings_music")) {
            settingsMusic = preferences.getInt("Settings_music", 1)
        } else {
            val editor = preferences.edit()
            settingsMusic = 1
            editor.putInt("Settings_music", 1)
            editor.apply()
        }

        if (preferences.contains("language")) {
            language = preferences.getString("language", "en").toString()
        } else {
            val editor = preferences.edit()
            language = Locale.getDefault().language
            editor.putString("language", language)
            editor.apply()
        }
    }

    fun changeSettingsMusic(n: Int){
        val editor = preferences.edit()
        settingsMusic = n
        editor.putInt("Settings_music", n)
        editor.apply()
    }

    fun changeSettingsLanguage(s: String){
        var lang =s
        if(lang!="ru") lang="en"
        val editor = preferences.edit()
        language = lang
        editor.putString("language",language)
        editor.apply()
    }

    companion object {

        @Volatile
        private lateinit var sharedPreferencesRepository: SharedPreferencesRepository

        fun init(context: Context) {
            synchronized(this) {
                sharedPreferencesRepository = SharedPreferencesRepository(context)
            }
        }

        fun get(): SharedPreferencesRepository {
            return sharedPreferencesRepository
        }
    }
}