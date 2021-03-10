package com.test.runar.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import com.test.runar.R

class MediaService: Service() {
    private lateinit var mediaPlayer: MediaPlayer
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.song)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer.start()
        Log.d(LOGCAT, "Media Player started!")
        if (mediaPlayer.isLooping() !== true) {
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
    companion object {
        private var LOGCAT: String? = null
    }
}