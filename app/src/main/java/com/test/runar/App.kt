package com.test.runar

import android.app.Application
import com.test.runar.repository.SharedPreferencesRepository
import com.test.runar.room.AppDB

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDB.init(this)
        SharedPreferencesRepository.init(this)
    }
}