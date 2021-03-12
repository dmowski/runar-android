package com.test.runar.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import com.test.runar.R
import java.security.SecureRandom
import java.util.*

class MediaService : Service() {

    var random = SecureRandom()
    var list: MutableList<Int> = Arrays.asList(R.raw.song, R.raw.song1, R.raw.song2, R.raw.song3)
    var randomSong = list.get(random.nextInt(list.size))

    private lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer.create(applicationContext, randomSong)
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

    companion object {
        private var LOGCAT: String? = null
    }
}