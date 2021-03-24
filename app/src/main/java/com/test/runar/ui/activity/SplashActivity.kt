package com.test.runar.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.test.runar.R
import com.test.runar.RunarLogger
import com.test.runar.controllers.MusicController
import com.test.runar.databinding.ActivitySplashBinding
import com.test.runar.presentation.viewmodel.SplashViewModel

class SplashActivity : AppCompatActivity(){

    private val viewModel: SplashViewModel by viewModels()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.library_top_bar)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        RunarLogger.logDebug("splash screen start music")
        playAudio()
        supportActionBar?.hide()
        setupViewModel()
    }

    fun playAudio() {
        MusicController.startMusic()
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
}

