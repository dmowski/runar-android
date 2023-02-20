package com.tnco.runar.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tnco.runar.R
import com.tnco.runar.databinding.ActivitySplashBinding
import com.tnco.runar.ui.fragment.OnboardFragment
import com.tnco.runar.ui.viewmodel.MusicControllerViewModel
import com.tnco.runar.ui.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()
    private val musicControllerViewModel: MusicControllerViewModel by viewModels()
    private lateinit var binding: ActivitySplashBinding
    private var musicState = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.library_top_bar)
        window.navigationBarColor = getColor(R.color.library_top_bar)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        setupViewModel()
    }

    override fun onResume() {
        musicControllerViewModel.updateSplashStatus(true)
        musicControllerViewModel.startMusic()
        super.onResume()
    }

    override fun onPause() {
        musicControllerViewModel.updateSplashStatus(false)
        musicControllerViewModel.softStopMusic()
        super.onPause()
    }

    private fun setupViewModel() {
        viewModel.progress.observe(this) { progress -> updateProgress(progress) }
        viewModel.splashCommand.observe(this) { launchMainActivity() }
    }

    private fun updateProgress(progress: Int) {
        binding.loadingProgress.progress = progress
    }

    private fun launchMainActivity() {
        if (viewModel.sharedPreferencesRepository.settingsOnboarding == 1) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.action_container, OnboardFragment()).commit()
        } else {
            intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        }
        musicState = false
        startActivity(intent)
        finish()
    }
}
