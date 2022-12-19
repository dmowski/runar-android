package com.tnco.runar.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.tnco.runar.RunarLogger
import java.util.*

class SharedPreferencesRepository private constructor(context: Context) {
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
    var switcherNames: Set<String> = setOf()

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

        val editor = preferences.edit()
        lastRunTime = System.currentTimeMillis()
        editor.putLong("last_run", lastRunTime)
        editor.apply()
        RunarLogger.logDebug("last_run $lastRunTime")

        if (preferences.contains(SWITCHER_NAMES_PREF)) {
            val receivedSwitcherNames = preferences.getStringSet(SWITCHER_NAMES_PREF, setOf())
            switcherNames = receivedSwitcherNames?.toSet() ?: setOf()
        } else {
            initDeveloperSwitchers()
        }
    }

    private fun initDeveloperSwitchers() {
        val switchers = mapOf(
            Pair("Test", false),
            Pair("Displaying audio fairy tales", true)
        )
        switcherNames = switchers.keys
        putSwitcherNames(switcherNames)
        putSwitcherStates(switchers)
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

    fun changeSettingsLanguage(s: String) {
        var lang = s
        if (lang != "ru") lang = "en"
        val editor = preferences.edit()
        language = lang
        editor.putString("language", language)
        editor.apply()
    }

    private fun putSwitcherNames(names: Set<String>) {
        preferences.edit().apply {
            putStringSet(SWITCHER_NAMES_PREF, names)
            apply()
        }
    }

    private fun putSwitcherStates(states: Map<String, Boolean>) {
        preferences.edit().apply {
            states.forEach {
                putBoolean(it.key, it.value)
            }
            apply()
        }
    }

    fun putSwitcherState(key: String, state: Boolean) {
        preferences.edit().apply {
            putBoolean(key, state)
            apply()
        }
    }

    fun switcherStates(): MutableMap<String, Boolean> {
        val result: MutableMap<String, Boolean> = mutableMapOf()
        val keys = preferences.getStringSet(SWITCHER_NAMES_PREF, setOf()) ?: setOf()
        keys.forEach {
            result[it] = preferences.getBoolean(it, false)
        }
        return result
    }

    internal fun hideAudioTales() {
        audioTalesDisplaying = false
    }

    companion object {

        private const val SWITCHER_NAMES_PREF = "switcher_names"
        var audioTalesDisplaying = true

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
