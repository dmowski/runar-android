package com.test.runar.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.test.runar.R
import com.test.runar.presentation.viewmodel.SplashViewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()
    private var progressBarLoading: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        progressBarLoading = findViewById(R.id.loadingProgress)
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.progress.observe(this, { progress -> updateProgress(progress) })
        viewModel.splashCommand.observe(this, { launchMainActivity() })
    }

    private fun updateProgress(progress: Int) {
        progressBarLoading?.progress = progress
    }

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
        finish()
    }
}

