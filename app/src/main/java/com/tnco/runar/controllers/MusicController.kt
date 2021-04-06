package com.tnco.runar.controllers

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.tnco.runar.R
import com.tnco.runar.repository.SharedPreferencesRepository
import kotlinx.coroutines.*
import java.util.*

object MusicController {

    private var musicList = arrayOf(
        R.raw.danheim_kala, R.raw.danheim_runar, R.raw.led_chernaya_ladya, R.raw.led_mat_moya_skazala
    )
    private lateinit var mediaPlayer: MediaPlayer
    var preferencesRepository = SharedPreferencesRepository.get()
    var currentSongPos=0
    var log1 = 0.25f

    var splashStatus = false
    var mainStatus = false

    fun init(context: Context) {
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
            mediaPlayer.reset()
            val mediaPath = Uri.parse("android.resource://" + context.packageName + "/" + musicList[currentSongPos])
            mediaPlayer.setDataSource(context,mediaPath)
            mediaPlayer.prepare()
            mediaPlayer.setVolume(log1,log1)
            if(preferencesRepository.settingsMusic==1){
                mediaPlayer.start()
            }
        }
    }

    private fun getRandomSongPos() : Int{
        val mGenerator = Random()
        return mGenerator.nextInt(musicList.size)
    }

    fun startMusic(){
        if(preferencesRepository.settingsMusic==1){
            if(!mediaPlayer.isLooping){
                if(splashStatus||mainStatus) mediaPlayer.start()
            }
        }
    }

    fun stopMusic(){
        mediaPlayer.pause()
    }

    fun softStopMusic(){
        CoroutineScope(Dispatchers.Default).launch {
            delay(300L)
            if(!splashStatus&&!mainStatus){
                mediaPlayer.pause()
            }
        }
    }
}