package com.test.runar.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.test.runar.R
import com.test.runar.databinding.FragmentLayoutProcessingBinding
import java.security.SecureRandom
import java.util.*

class MediaService : Service() {

    private val binder = LocalBinder()
 private var list= arrayOf(
        R.raw.lose_yourself, R.raw.monster, R.raw.not_afraid, R.raw.stan
    )

    companion object {
        private var LOGCAT: String? = null
    }
    private lateinit var mediaPlayer: MediaPlayer

    private val rand = Random()

    fun getRandArrayElement(): Int {
        return list.get(rand.nextInt(list.size))
    }
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(applicationContext, getRandArrayElement())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer.start()
        Log.d(LOGCAT, "Media Player started!")
        if (!mediaPlayer.isLooping) {
            Log.d(LOGCAT, "Problem in Playing Audio")
        }
        return START_STICKY
    }

    fun onStop() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    fun onPause() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): MediaService= this@MediaService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }
}