package com.tnco.runar.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.tnco.runar.R
import com.tnco.runar.RunarLogger
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.ui.activity.MainActivity
import java.time.LocalDate


class PushService : FirebaseMessagingService() {

    private val preferencesRepository = SharedPreferencesRepository.get()

    override fun onNewToken(token: String) {
        RunarLogger.logDebug("Refreshed token: $token")
        Log.d("KEYKAK", "enter new token method")
        super.onNewToken(token)
    }

    override fun handleIntent(intent: Intent?) {
        RunarLogger.logDebug("received message from server")
        Log.d("KEYKAK", "enter handle intent method")
        //crutch, for some reason the notification comes on the first start
        //if I open just only once, it will be problem
        if (preferencesRepository.firstLaunch != 1 && isShouldSend()) {
            sendNotification()
        }
    }

    private fun isShouldSend(): Boolean {
        var isShouldSend = false
        val monday = "MONDAY"
        val currentDay = LocalDate.now().dayOfWeek.name
        val currentTime = System.currentTimeMillis()
        val lastRunTime = preferencesRepository.lastRunTime
        val startTimeNotification = 11
        val oneHour = 3600000L
        val difference = oneHour * startTimeNotification
        val isOk = currentTime - lastRunTime > difference
        if (currentDay == monday && isOk) {
            isShouldSend = true
        }
        return isShouldSend
    }

    private fun sendNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = getString(R.string.push_general_notification_channel_id)
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notif_icon_white)
            .setContentText(getString(R.string.push_general_notification))
            .setAutoCancel(true)
//            .setStyle()//create style for day and night mode
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(manager)
        manager.notify(0, builder.build())
    }

    //settings still empty
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.push_general_notification_channel_id)
            val name = getString(R.string.push_general_notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance)
            notificationManager.createNotificationChannel(channel)
        }
    }

}