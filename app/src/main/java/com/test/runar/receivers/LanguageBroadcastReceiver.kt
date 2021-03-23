package com.test.runar.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.test.runar.RunarLogger
import com.test.runar.repository.DatabaseRepository
import com.test.runar.repository.SharedDataRepository
import com.test.runar.room.AppDB

class LanguageBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        AppDB.init(context!!)
        DatabaseRepository.reinit()
        SharedDataRepository.init(context)
        RunarLogger.logDebug("receive change language")
    }
}