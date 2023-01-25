package com.tnco.runar.feature

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.tnco.runar.R
import com.tnco.runar.repository.SharedPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicController @Inject constructor(
    @ApplicationContext context: Context,
) {

    private var musicList = arrayOf(
        R.raw.danheim_kala,
        R.raw.danheim_runar,
        R.raw.led_chernaya_ladya,
        R.raw.led_mat_moya_skazala
    )
    private var mediaPlayer: MediaPlayer
    private var preferencesRepository = SharedPreferencesRepository.get()
    private var currentSongPos = 0
    private var log1 = 0.25f

    private var splashStatus = false
    private var mainStatus = false
    private var onboardingStatus = false

    init {
        currentSongPos = getRandomSongPos()
        mediaPlayer = MediaPlayer.create(context, musicList[currentSongPos])
        mediaPlayer.setVolume(log1, log1)

        mediaPlayer.setOnCompletionListener {
            var state = true
            while (state) {
                val newSongPos = getRandomSongPos()
                if (newSongPos == currentSongPos) continue
                else {
                    currentSongPos = newSongPos
                    state = false
                }
            }
            mediaPlayer.reset()
            val mediaPath =
                Uri.parse("android.resource://" + context.packageName + "/" + musicList[currentSongPos])
            mediaPlayer.setDataSource(context, mediaPath)
            mediaPlayer.prepare()
            mediaPlayer.setVolume(log1, log1)
            if (preferencesRepository.settingsMusic == 1) {
                mediaPlayer.start()
            }
        }
    }

    fun currentSongPos() = currentSongPos

    fun updateSplashStatus(status: Boolean) {
        splashStatus = status
    }

    fun updateMainStatus(status: Boolean) {
        mainStatus = status
    }

    fun updateOnboardingStatus(status: Boolean) {
        onboardingStatus = status
    }

    private fun getRandomSongPos(): Int {
        val mGenerator = Random()
        return mGenerator.nextInt(musicList.size)
    }

    fun startMusic() {
        if (preferencesRepository.settingsMusic == 1) {
            if (!mediaPlayer.isLooping) {
                if (splashStatus || mainStatus || onboardingStatus) mediaPlayer.start()
            }
        }
    }

    fun stopMusic() {
        mediaPlayer.pause()
    }

    fun softStopMusic() {
        CoroutineScope(Dispatchers.Default).launch {
            delay(500L)
            if (!splashStatus && !mainStatus && !onboardingStatus) {
                mediaPlayer.pause()
            }
        }
    }
}
