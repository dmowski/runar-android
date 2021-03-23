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

    private var list = arrayOf(
        R.raw.danheim_kala, R.raw.danheim_runar, R.raw.led_chernaya_ladya, R.raw.led_mat_moya_skazala
    )

    companion object {
        private var LOGCAT: String? = null
    }

    private lateinit var mediaPlayer: MediaPlayer

    // Random number generator
    private val mGenerator = Random()
    var randomSong= list.get(mGenerator.nextInt(list.size))

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

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}