package com.tnco.runar.ui.activity

import android.animation.ArgbEvaluator
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.TypedValue
import android.view.WindowManager
import android.widget.Adapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.tnco.runar.R
import com.tnco.runar.adapters.OnboardViewPagerAdapter
import com.tnco.runar.databinding.ActivityMainBinding
import com.tnco.runar.databinding.ActivityOnboardBinding
import com.tnco.runar.model.OnboardGuideElementModel
import com.tnco.runar.presentation.viewmodel.OnboardViewModel
import com.tnco.runar.repository.LanguageRepository
import java.util.ArrayList

class OnboardActivity : AppCompatActivity() {
    private val viewModel: OnboardViewModel by viewModels()
    private var fontSize: Float = 0f

    lateinit var adapter: OnboardViewPagerAdapter
    lateinit var models: ArrayList<OnboardGuideElementModel>
    var argbEvaluator = ArgbEvaluator()

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

        binding.skipButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
        }

        viewModel.fontSize.observe(this){
            fontSize = it
            binding.skipButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (it * 0.7).toFloat())
            models = ArrayList<OnboardGuideElementModel>()
            models.add(OnboardGuideElementModel(getString(R.string.onboard_next)))
            models.add(OnboardGuideElementModel(getString(R.string.onboard_next)))
            models.add(OnboardGuideElementModel(getString(R.string.onboard_next)))
            models.add(OnboardGuideElementModel(getString(R.string.onboard_next)))
            models.add(OnboardGuideElementModel(getString(R.string.onboard_begin)))

            adapter = OnboardViewPagerAdapter(models,this,it)
            binding.viewPager.adapter = adapter
        }
        binding.viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }
}