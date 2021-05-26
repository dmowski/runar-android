package com.tnco.runar.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tnco.runar.R
import com.tnco.runar.databinding.ActivityMainBinding
import com.tnco.runar.databinding.ActivityOnboardBinding
import com.tnco.runar.presentation.viewmodel.OnboardViewModel
import com.tnco.runar.repository.LanguageRepository

class OnboardActivity : AppCompatActivity() {
    private val viewModel: OnboardViewModel by viewModels()
    private var fontSize: Float = 0f

    private lateinit var binding: ActivityOnboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LanguageRepository.setSettingsLanguage(this)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.library_top_bar)
        window.navigationBarColor = getColor(R.color.library_top_bar)

        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel.fontSize.observe(this){
            fontSize = it
        }


    }
}