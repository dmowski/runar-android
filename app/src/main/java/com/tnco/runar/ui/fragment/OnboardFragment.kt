package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.tnco.runar.R
import com.tnco.runar.databinding.FragmentOnboardBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.model.OnboardGuideElementModel
import com.tnco.runar.ui.adapter.OnboardViewPagerAdapter
import com.tnco.runar.ui.viewmodel.MusicControllerViewModel
import com.tnco.runar.ui.viewmodel.OnboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardFragment : Fragment() {

    private val viewModel: OnboardViewModel by viewModels()
    private val musicControllerViewModel: MusicControllerViewModel by viewModels()

    private var currentPosition = 0

    private lateinit var adapter: OnboardViewPagerAdapter
    private lateinit var models: ArrayList<OnboardGuideElementModel>

    private var _binding: FragmentOnboardBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.skipButton.setOnClickListener {
            viewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_SKIP)
            finishOnboard()
        }

        viewModel.changeCurrentPosition(0)
        viewModel.nextActivity(false)

        viewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_ABOUT_OPENED)

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

        adapter = OnboardViewPagerAdapter(models, ::onChangeActivity, ::onChangePosition)
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
                    0 -> viewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_ABOUT_OPENED)
                    1 -> viewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_FORTUNE_OPENED)
                    2 -> viewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_INTERPRETATION_OPENED)
                    3 -> viewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_FAVOURITES_OPENED)
                    4 -> viewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_GENERATOR_OPENED)
                    5 -> viewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_LIBRARY_OPENED)
                }
                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        viewModel.currentPosition.observe(viewLifecycleOwner) {
            binding.viewPager.setCurrentItem(it, true)
            changeSelectionCircle(it)
            binding.skipButton.isVisible = it != 5
        }
        viewModel.end.observe(viewLifecycleOwner) {
            if (it == true) {
                finishOnboard()
            }
        }
    }
    private fun finishOnboard() {
        val direction = OnboardFragmentDirections.actionOnboardFragmentToRunicDraws()
        findNavController().navigate(direction)
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
        musicControllerViewModel.updateOnboardingStatus(true)
        musicControllerViewModel.startMusic()
        super.onResume()
    }

    override fun onPause() {
        musicControllerViewModel.updateOnboardingStatus(false)
        musicControllerViewModel.softStopMusic()
        super.onPause()
    }

    private fun onChangeActivity() {
        viewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_START)
        viewModel.nextActivity(true)
    }

    private fun onChangePosition(position: Int) {
        viewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_NEXT)
        viewModel.changeCurrentPosition(position + 1)
    }
}
