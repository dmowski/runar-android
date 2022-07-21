package com.tnco.runar.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tnco.runar.R
import com.tnco.runar.RunarLogger
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.ui.activity.MainActivity


class PushService : FirebaseMessagingService() {

//    private val preferencesRepository = SharedPreferencesRepository.get()

    override fun onNewToken(token: String) {
        RunarLogger.logDebug("Refreshed token: $token")
        super.onNewToken(token)
    }


    override fun onMessageReceived(message: RemoteMessage) {
        RunarLogger.logDebug("From: ${message.from}")
//        val title = message.data["title"]
//        val text = message.data["body_loc_key"]
//        sendNotification(text)
    }

    override fun handleIntent(intent: Intent?) {
        RunarLogger.logDebug("received message from server")//заходит при первой установке
//        if(preferencesRepository.lastRunTime<System.currentTimeMillis()){
//            sendNotification()
//        }
        sendNotification()

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
//            .setContentTitle(getString(R.string.title_push_general_notification))
            .setContentText(getString(R.string.title_push_general_notification))
//            .setContentText(getString(R.string.push_general_notification))
            .setAutoCancel(true)//автоматом удалит нотификацию после открытия
//            .setStyle()//create
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)
        notificationManager.notify(0, builder.build())
    }

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