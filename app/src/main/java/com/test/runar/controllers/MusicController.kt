package com.test.runar.controllers

import android.content.Context
import android.media.MediaPlayer
import com.test.runar.R
import com.test.runar.RunarLogger
import com.test.runar.repository.SharedPreferencesRepository
import java.util.*

object MusicController {

    private var musicList = arrayOf(
        R.raw.danheim_kala, R.raw.danheim_runar, R.raw.led_chernaya_ladya, R.raw.led_mat_moya_skazala
    )
    private lateinit var mediaPlayer: MediaPlayer
    var preferencesRepository = SharedPreferencesRepository.get()

    fun init(context: Context) {
        mediaPlayer = MediaPlayer.create(context, getRandomSong())
    }

    private fun getRandomSong() : Int{
        val mGenerator = Random()
        return musicList[mGenerator.nextInt(musicList.size)]
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