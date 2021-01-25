package com.test.runar.repository

import android.app.Application
import androidx.preference.PreferenceManager
import java.util.*

class SharedPreferencesRepository(application: Application) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(application)

    fun getUserId(): String{
        var userId: String =""
        if(preferences.contains("Id")){
            userId = preferences.getString("Id","").toString()
        }
        else{
            val editor = preferences.edit()
            userId = UUID.randomUUID().toString()
            editor.putString("Id",userId)
            editor.apply()
        }
        return userId
    }
}