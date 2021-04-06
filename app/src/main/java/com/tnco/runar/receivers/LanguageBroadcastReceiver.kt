package com.tnco.runar.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.room.AppDB
import java.util.*

class LanguageBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        AppDB.init(context!!)
        DatabaseRepository.reinit()
        SharedDataRepository.init(context)
        val spr = SharedPreferencesRepository.get()
        spr.changeSettingsLanguage(Locale.getDefault().language)
    }
}