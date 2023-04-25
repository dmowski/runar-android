package com.tnco.runar.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.tnco.runar.RunarLogger
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    val userId: String
    var settingsMusic: Int
    var settingsOnboarding: Int
    var minRuneLvl: Int
    var firstLaunch: Int = 0
    var language: String
    var lastRunTime: Long
    var lastDivination: Long = 0
    var countingStartDate: Long = 0
    var runicLayoutsLimit: Int = 0

    init {
        val appVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionCode
        if (preferences.contains("version")) {
            val oldVersion = preferences.getInt("version", 0)
            if (appVersion > oldVersion) {
                val editor = preferences.edit()
                editor.clear()
                editor.putInt("version", appVersion)
                editor.apply()
                context.deleteDatabase("RU_DATABASE")
                context.deleteDatabase("EN_DATABASE")
            }
        } else {
            val editor = preferences.edit()
            editor.putInt("version", appVersion)
            editor.apply()
        }
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

        if (preferences.contains("Settings_onboarding")) {
            settingsOnboarding = preferences.getInt("Settings_onboarding", 1)
        } else {
            firstLaunch = 1
            val editor = preferences.edit()
            settingsOnboarding = 1
            editor.putInt("Settings_onboarding", 1)
            editor.apply()
        }

        if (preferences.contains("Min_rune_lvl")) {
            minRuneLvl = preferences.getInt("Min_rune_lvl", 0)
        } else {
            minRuneLvl = 0
            val editor = preferences.edit()
            editor.putInt("Min_rune_lvl", minRuneLvl)
            editor.apply()
        }

        if (preferences.contains("language")) {
            language = preferences.getString("language", "en").toString()
        } else {
            val editor = preferences.edit()
            val androidLanguage = Locale.getDefault().language
            language = if (androidLanguage != "ru") {
                "en"
            } else {
                "ru"
            }
            RunarLogger.logDebug(language)
            editor.putString("language", language)
            editor.apply()
        }

        if (preferences.contains("start_counting_date")) {
            countingStartDate = preferences.getLong("start_counting_date", 0)
        } else {
            countingStartDate = 0
            val editor = preferences.edit()
            editor.putLong("start_counting_date", countingStartDate)
            editor.apply()
        }

        if (preferences.contains("runic_draws_limit_count")) {
            runicLayoutsLimit = preferences.getInt("runic_draws_limit_count", 0)
        } else {
            runicLayoutsLimit = 3
            val editor = preferences.edit()
            editor.putInt("runic_draws_limit_count", runicLayoutsLimit)
            editor.apply()
        }

        val editor = preferences.edit()
        lastRunTime = System.currentTimeMillis()
        editor.putLong("last_run", lastRunTime)
        editor.apply()
        RunarLogger.logDebug("last_run $lastRunTime")
    }

    fun getLibHash(lng: String): String {
        return if (preferences.contains("${lng}_library_hash")) {
            preferences.getString("${lng}_library_hash", "").toString()
        } else {
            val editor = preferences.edit()
            editor.putString("${lng}_library_hash", "")
            editor.apply()
            ""
        }
    }

    fun putLibHash(lng: String, hash: String) {
        val editor = preferences.edit()
        editor.putString("${lng}_library_hash", hash)
        editor.apply()
    }

    fun putLastTimeDivination(time: Long) {
        val editor = preferences.edit()
        lastDivination = time
        editor.putLong("lastDivination", lastDivination)
        editor.apply()
    }

    fun changeSettingsMusic(n: Int) {
        val editor = preferences.edit()
        settingsMusic = n
        editor.putInt("Settings_music", n)
        editor.apply()
    }

    fun changeSettingsOnboarding(n: Int) {
        val editor = preferences.edit()
        settingsOnboarding = n
        editor.putInt("Settings_onboarding", n)
        editor.apply()
    }

    fun changeStartCountingDate(l: Long) {
        val editor = preferences.edit()
        countingStartDate = l
        editor.putLong("start_counting_date", countingStartDate)
        editor.apply()
    }

    fun changeLimit(count: Int) {
        val editor = preferences.edit()
        runicLayoutsLimit = count
        editor.putInt("runic_draws_limit_count", runicLayoutsLimit)
        editor.apply()
    }
}
