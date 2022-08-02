package com.tnco.runar.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.graphics.drawable.toBitmap
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
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
        val lastRunTime = preferencesRepository.lastDivination
        val startTimeNotification = 11
        val oneHour = 3600000L
        val difference = oneHour * startTimeNotification
        val isOk = currentTime - lastRunTime > difference
        Log.d("KEYKAK", "$difference, $currentDay, ${currentTime - lastRunTime}")
        if (currentDay == monday && isOk) {
            Log.d("KEYKAK", "isOk=true")
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
        val largeIcon = ResourcesCompat.getDrawable(resources, R.drawable.large_icon, null)
        val bigLargeIcon = ResourcesCompat.getDrawable(resources, R.drawable.big_large_icon, null)
        val channelId = getString(R.string.push_general_notification_channel_id)
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notif_icon_white)
            .setContentText(getString(R.string.push_general_notification))
            .setAutoCancel(true)
            .setLargeIcon(largeIcon?.toBitmap(R.dimen.large_icon_width, R.dimen.large_icon_height))
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(
                        bigLargeIcon?.toBitmap(
                            R.dimen.big_large_icon_width,
                            R.dimen.big_large_icon_height
                        )
                    )
                    .bigLargeIcon(null)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(manager)
        manager.notify(0, builder.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.push_general_notification_channel_id)
            val name = getString(R.string.push_general_notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance)
            notificationManager.createNotificationChannel(channel)
        }
    }

}