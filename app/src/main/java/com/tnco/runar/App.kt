package com.tnco.runar

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.data.local.AppDB
import com.tnco.runar.repository.LanguageRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.services.PushService.Companion.REMINDER_CHANNEL_ID
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var languageRepository: LanguageRepository

    @Inject
    lateinit var sharedDataRepository: SharedDataRepository

    override fun onCreate() {
        AppDB.init(this)
        super.onCreate()
        sharedDataRepository.init(this)
        languageRepository.setInitialSettingsLanguage(this)
        createNotificationChannel()
        AnalyticsHelper.init()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("KEYKAK", "in create notification channel")
            val name = getString(R.string.push_general_notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(REMINDER_CHANNEL_ID, name, importance)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}
