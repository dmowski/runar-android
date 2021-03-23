package com.test.runar.controllers

import android.content.Context
import android.media.MediaPlayer
import com.test.runar.R
import com.test.runar.RunarLogger
import com.test.runar.repository.SharedPreferencesRepository
import java.util.*
import kotlin.math.ln

object MusicController {

    private var musicList = arrayOf(
        R.raw.danheim_kala, R.raw.danheim_runar, R.raw.led_chernaya_ladya, R.raw.led_mat_moya_skazala
    )
    private lateinit var mediaPlayer: MediaPlayer
    var preferencesRepository = SharedPreferencesRepository.get()
    var currentSongPos=0
    var log1 = 0.015f

    fun init(context: Context) {
        RunarLogger.logDebug(log1.toString())
        currentSongPos = getRandomSongPos()
        mediaPlayer = MediaPlayer.create(context, musicList[currentSongPos])
        mediaPlayer.setVolume(log1,log1)

        mediaPlayer.setOnCompletionListener {
            var state = true
            while(state){
                val newSongPos = getRandomSongPos()
                if(newSongPos== currentSongPos) continue
                else {
                    currentSongPos = newSongPos
                    state = false
                }
            }
            mediaPlayer = MediaPlayer.create(context, musicList[currentSongPos])
            mediaPlayer.setVolume(log1,log1)
            mediaPlayer.start()
        }
    }

    private fun getRandomSongPos() : Int{
        val mGenerator = Random()
        return mGenerator.nextInt(musicList.size)
    }

    fun startMusic(){
        if(preferencesRepository.settingsMusic==1){
            if(!mediaPlayer.isLooping){
                mediaPlayer.start()
                RunarLogger.logDebug("Media Player started!")
                if (!mediaPlayer.isLooping) {
                    RunarLogger.logDebug("Problem in Playing Audio")
                }
            }
        }
    }

    fun stopMusic(){
        mediaPlayer.pause()
    }
}