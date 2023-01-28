package com.tnco.runar.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tnco.runar.data.local.AppDB
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LanguageBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var databaseRepository: DatabaseRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        AppDB.init(context!!)
        databaseRepository.reinit()
        SharedDataRepository.init(context)
        val spr = SharedPreferencesRepository.get()
        spr.changeSettingsLanguage(Locale.getDefault().language)
    }
}
