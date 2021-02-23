package com.test.runar

import android.app.Application
import com.test.runar.repository.SharedDataRepository
import com.test.runar.repository.SharedPreferencesRepository
import com.test.runar.room.AppDB
import com.test.runar.room.DataDB

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDB.init(this)
        DataDB.init(this)
        SharedPreferencesRepository.init(this)
        SharedDataRepository.init(this)
    }
}