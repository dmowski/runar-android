package com.test.runar.repository

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.util.*

class SharedPreferencesRepository(application: Application) {
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

    val userId: String

    init {
        if (preferences.contains("Id")) {
            userId = preferences.getString("Id", "").toString()
        } else {
            val editor = preferences.edit()
            userId = UUID.randomUUID().toString()
            editor.putString("Id", userId)
            editor.apply()
        }
    }
}