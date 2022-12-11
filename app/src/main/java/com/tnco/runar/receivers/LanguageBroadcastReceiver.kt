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
    lateinit var sharedDataRepository: SharedDataRepository

    @Inject
    lateinit var sharedPreferencesRepository: SharedPreferencesRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        AppDB.init(context!!) // TODO
        DatabaseRepository.reinit()
        sharedDataRepository.init(context)
        sharedPreferencesRepository.changeSettingsLanguage(Locale.getDefault().language)
    }
}
