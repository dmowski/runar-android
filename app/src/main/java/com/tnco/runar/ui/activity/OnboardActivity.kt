package com.tnco.runar.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.databinding.ActivityOnboardBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.feature.MusicController
import com.tnco.runar.model.OnboardGuideElementModel
import com.tnco.runar.repository.LanguageRepository
import com.tnco.runar.ui.adapter.OnboardViewPagerAdapter
import com.tnco.runar.ui.viewmodel.OnboardViewModel

class OnboardActivity : AppCompatActivity() {
    private val viewModel: OnboardViewModel by viewModels()
    private var currentPosition = 0

    private lateinit var adapter: OnboardViewPagerAdapter
    private lateinit var models: ArrayList<OnboardGuideElementModel>

    private lateinit var binding: ActivityOnboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LanguageRepository.setSettingsLanguage(this)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.library_top_bar)
        window.navigationBarColor = getColor(R.color.library_top_bar)

        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel.changeCurrentPosition(0)
        viewModel.nextActivity(false)

        AnalyticsHelper.sendEvent(AnalyticsEvent.OB_ABOUT_OPENED)

        binding.skipButton.setOnClickListener {
            AnalyticsHelper.sendEvent(AnalyticsEvent.OB_SKIP)
            closeActivity()
        }

        models = ArrayList()
        models.add(
            OnboardGuideElementModel(
                getString(R.string.onboard_next),
                getString(R.string.onboard_header1),
                getString(R.string.onboard_text1),
                R.drawable.onboard_1
            )
        )
        models.add(
            OnboardGuideElementModel(
                getString(R.string.onboard_next),
                getString(R.string.onboard_header2),
                getString(R.string.onboard_text2),
                R.drawable.onboard_2
            )
        )
        models.add(
            OnboardGuideElementModel(
                getString(R.string.onboard_next),
                getString(R.string.onboard_header3),
                getString(R.string.onboard_text3),
                R.drawable.onboard_3
            )
        )
        models.add(
            OnboardGuideElementModel(
                getString(R.string.onboard_next),
                getString(R.string.onboard_header4),
                getString(R.string.onboard_text4),
                R.drawable.onboard_4
            )
        )
        models.add(
            OnboardGuideElementModel(
                getString(R.string.onboard_next),
                getString(R.string.onboard_header5),
                getString(R.string.onboard_text5),
                R.drawable.onboard_5
            )
        )
        models.add(
            OnboardGuideElementModel(
                getString(R.string.onboard_begin),
                getString(R.string.onboard_header6),
                getString(R.string.onboard_text6),
                R.drawable.onboard_6
            )
        )

        adapter = OnboardViewPagerAdapter(models, this, viewModel)
        binding.viewPager.adapter = adapter

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                viewModel.changeCurrentPosition(position)
                when (position) {
                    0 -> AnalyticsHelper.sendEvent(AnalyticsEvent.OB_ABOUT_OPENED)
                    1 -> AnalyticsHelper.sendEvent(AnalyticsEvent.OB_FORTUNE_OPENED)
                    2 -> AnalyticsHelper.sendEvent(AnalyticsEvent.OB_INTERPRETATION_OPENED)
                    3 -> AnalyticsHelper.sendEvent(AnalyticsEvent.OB_FAVOURITES_OPENED)
                    4 -> AnalyticsHelper.sendEvent(AnalyticsEvent.OB_LIBRARY_OPENED)
                    5 -> AnalyticsHelper.sendEvent(AnalyticsEvent.OB_GENERATOR_OPENED)
                }
                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        viewModel.currentPosition.observe(this) {
            binding.viewPager.setCurrentItem(it, true)
            changeSelectionCircle(it)
            binding.skipButton.isVisible = it != 5
        }
        viewModel.end.observe(this) {
            if (it == true) {
                closeActivity()
            }
        }
    }

    override fun onBackPressed() {
        if (currentPosition in 1..5) {
            viewModel.changeCurrentPosition(currentPosition - 1)
        } else {
            super.onBackPressed()
        }
    }

    private fun closeActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }

    private fun changeSelectionCircle(position: Int) {
        for (i in 0 until binding.circlesLayout.childCount) {
            (binding.circlesLayout.getChildAt(i) as ImageView)
                .setImageResource(R.drawable.ic_point_deselected)
        }
        (binding.circlesLayout.getChildAt(position) as ImageView)
            .setImageResource(R.drawable.ic_point_selected)
    }

    override fun onResume() {
        MusicController.onboardingStatus = true
        MusicController.startMusic()
        super.onResume()
    }

    override fun onPause() {
        MusicController.onboardingStatus = false
        MusicController.softStopMusic()
        super.onPause()
    }
}