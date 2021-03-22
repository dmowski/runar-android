package com.test.runar.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.test.runar.R
import com.test.runar.databinding.ActivitySplashBinding
import com.test.runar.presentation.viewmodel.SplashViewModel
import com.test.runar.service.MediaService
import com.test.runar.service.RandomValue
import com.test.runar.ui.fragments.LayoutProcessingFragment
import kotlin.properties.Delegates

class SplashActivity : AppCompatActivity(), RandomValue {

    private val viewModel: SplashViewModel by viewModels()
    private lateinit var binding: ActivitySplashBinding
    private var mediaService  = MediaService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var fragment = LayoutProcessingFragment()

        playAudio(view)
        setupViewModel()
    }

    fun playAudio(view: View?) {
        val objIntent = Intent(this, MediaService::class.java)
        startService(objIntent)
    }

    private fun setupViewModel() {
        viewModel.progress.observe(this, { progress -> updateProgress(progress) })
        viewModel.splashCommand.observe(this, { launchMainActivity() })
    }

    private fun updateProgress(progress: Int) {
        binding.loadingProgress.progress = progress
    }

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
        finish()
    }

    override fun randomNumber(): Int {
       return mediaService.randomNumber()
    }
}

