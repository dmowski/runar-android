package com.tnco.runar.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tnco.runar.R
import com.tnco.runar.RunarLogger
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.ui.activity.MainActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class PushService : FirebaseMessagingService() {

    private val preferencesRepository = SharedPreferencesRepository.get()

    override fun onNewToken(token: String) {
        RunarLogger.logDebug("Refreshed token: $token")
        Log.d("KEYKAK", "enter new token method")
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("KEYKAK", "onMessageReceived - ${message.notification?.body}")
    }

    override fun handleIntent(intent: Intent?) {
        RunarLogger.logDebug("received message from server")
        Log.d("KEYKAK", "enter handle intent method")
        Log.d("KEYKAK", "keySet - ${intent?.extras?.keySet()?.joinToString(" ")}")
        Log.d(
            "KEYKAK",
            "gcm.notification.title - ${intent?.extras?.getString("gcm.notification.title")}"
        )
        Log.d(
            "KEYKAK",
            "gcm.notification.body - ${intent?.extras?.getString("gcm.notification.body")}"
        )
        // crutch, for some reason the notification comes on the first start
        // if I open just only once, it will be problem
        if (isShouldSend()) {
            sendNotification(intent)
        }
    }

    private fun isShouldSend(): Boolean {
        var isShouldSend = false
        val currentDay = getCurrentDay()
        val currentTime = System.currentTimeMillis()
        val lastRunTime = preferencesRepository.lastDivination
        val startTimeNotification = 11
        val oneHour = 3600000L
        val difference = oneHour * startTimeNotification
        val isOk = (currentTime - lastRunTime) > difference
        Log.d("KEYKAK", "$difference, $currentDay, ${currentTime - lastRunTime}")
        if (isOk) {
            Log.d("KEYKAK", "isOk=true")
            isShouldSend = true
        }
        return isShouldSend
    }

    private fun getCurrentDay(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("KEYKAK", "build version: greater than O")
            LocalDate.now().dayOfWeek.name.lowercase()
        } else {
            Log.d("KEYKAK", "build version: less than O")
            val format = SimpleDateFormat("EEEE", Locale.ENGLISH)
            val date = Date()
            format.format(date).lowercase()
        }
    }

    private fun sendNotification(intent: Intent?) {
        val notification = createNotification(intent)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val uniqueNotificationId = 0
        manager.notify(uniqueNotificationId, notification.build())
    }

    private fun createNotification(notificationInfo: Intent?): NotificationCompat.Builder {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val largeIcon = ResourcesCompat.getDrawable(resources, R.drawable.large_icon, null)
        val bigLargeIcon = ResourcesCompat.getDrawable(resources, R.drawable.big_large_icon, null)
        return NotificationCompat.Builder(this, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.notif_icon_white)
            .setContentText(
                if (notificationInfo?.extras?.getString("gcm.notification.body") != null)
                    notificationInfo.extras?.getString("gcm.notification.body")
                else
                    getString(R.string.push_general_notification)
            )
            .setAutoCancel(true)
            .setLargeIcon(largeIcon?.toBitmap())
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bigLargeIcon?.toBitmap())
                    .bigLargeIcon(null)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
    }

    companion object {
        const val REMINDER_CHANNEL_ID = "reminder_channel"
    }
}
