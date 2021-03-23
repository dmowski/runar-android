package com.test.runar.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.test.runar.databinding.ActivitySplashBinding
import com.test.runar.presentation.viewmodel.SplashViewModel
import com.test.runar.service.MediaService
import com.test.runar.ui.fragments.LayoutProcessingFragment

class SplashActivity : AppCompatActivity(){

    private val viewModel: SplashViewModel by viewModels()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        playAudio(view)
        supportActionBar?.hide()
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
}

