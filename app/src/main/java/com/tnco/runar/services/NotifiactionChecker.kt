package com.tnco.runar.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.tnco.runar.R
import com.tnco.runar.RunarLogger
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.ui.activity.SplashActivity

class NotifiactionChecker: Service() {
    private val preferencesRepository = SharedPreferencesRepository.get()

    private val MILLISECS_PER_DAY = 86400000L
    private val MILLISECS_PER_MIN = 60000L
    private val delay = MILLISECS_PER_MIN * 1
    //private val delay = MILLISECS_PER_DAY * 3

    override fun onCreate() {
        super.onCreate()
        RunarLogger.logDebug("service started!")
        if(preferencesRepository.lastRunTime<System.currentTimeMillis()){
            sendNotification()
        }
        setAlarm()
        RunarLogger.logDebug("service stopped")
        stopSelf();
    }
    fun setAlarm(){
        val serviceIntent = Intent(this,NotifiactionChecker::class.java)
        val pendingIntent = PendingIntent.getService(this,1995,serviceIntent,PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+delay, pendingIntent)
        RunarLogger.logDebug("alarm set!")
    }
    fun sendNotification(){
        var startIntent = Intent(this,SplashActivity::class.java)
        var notification = Notification.Builder(this)
            .setAutoCancel(false)
            .setContentIntent(PendingIntent.getActivities(this,1995,
                arrayOf(startIntent),PendingIntent.FLAG_UPDATE_CURRENT))
            .setContentTitle("We Miss You!")
            .setContentText("Please play our game again soon.")
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_settings)
            .setTicker("We Miss You! Please come back and play our game again soon.")
            .setWhen(System.currentTimeMillis())
            .build()
        var notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1995,notification)
        RunarLogger.logDebug("notification send")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}