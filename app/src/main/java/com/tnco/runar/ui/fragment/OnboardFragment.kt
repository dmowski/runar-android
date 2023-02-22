package com.tnco.runar.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

    private val onboardViewModel: OnboardViewModel by viewModels()
    private val musicControllerViewModel: MusicControllerViewModel by viewModels()

    private var currentPosition = 0

    private lateinit var adapter: OnboardViewPagerAdapter
    private lateinit var models: ArrayList<OnboardGuideElementModel>

    private lateinit var onOnboardFinishedListener: OnOnboardFinishedListener

    private var _binding: FragmentOnboardBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var onboardFinishedListener: OnOnboardFinishedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        super.onAttach(context)
        onboardFinishedListener = requireActivity() as OnOnboardFinishedListener
    }

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
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.library_top_bar)
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.library_top_bar)

        onboardViewModel.changeCurrentPosition(0)

        onboardViewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_ABOUT_OPENED)

        setupSkipButtonClickListener()

        initModels()

        adapter = OnboardViewPagerAdapter(models, ::onStartMainActivity, ::onChangePosition)
        binding.viewPager.adapter = adapter

        viewPagerChangeListener()
        onboardViewModelObservers()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSkipButtonClickListener() {
        binding.skipButton.setOnClickListener {
            onboardViewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_SKIP)
            onboardViewModel.shouldCloseScreen()
        }
    }

    private fun viewPagerChangeListener() {
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                onboardViewModel.changeCurrentPosition(position)
                when (position) {
                    0 -> onboardViewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_ABOUT_OPENED)
                    1 -> onboardViewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_FORTUNE_OPENED)
                    2 -> onboardViewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_INTERPRETATION_OPENED)
                    3 -> onboardViewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_FAVOURITES_OPENED)
                    4 -> onboardViewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_GENERATOR_OPENED)
                    5 -> onboardViewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_LIBRARY_OPENED)
                }
                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    private fun onboardViewModelObservers() {
        onboardViewModel.currentPosition.observe(viewLifecycleOwner) {
            binding.viewPager.setCurrentItem(it, true)
            changeSelectionCircle(it)
            binding.skipButton.isVisible = it != 5
        }
        onboardViewModel.endPosition.observe(viewLifecycleOwner) {
            if (it) {
                shouldCloseScreen()
            }
        }
    }

    private fun initModels() {
        models = ArrayList<OnboardGuideElementModel>().apply {
            add(
                OnboardGuideElementModel(
                    getString(R.string.onboard_next),
                    getString(R.string.onboard_header1),
                    getString(R.string.onboard_text1),
                    R.drawable.onboard_1
                )
            )
            add(
                OnboardGuideElementModel(
                    getString(R.string.onboard_next),
                    getString(R.string.onboard_header2),
                    getString(R.string.onboard_text2),
                    R.drawable.onboard_2
                )
            )
            add(
                OnboardGuideElementModel(
                    getString(R.string.onboard_next),
                    getString(R.string.onboard_header3),
                    getString(R.string.onboard_text3),
                    R.drawable.onboard_3
                )
            )
            add(
                OnboardGuideElementModel(
                    getString(R.string.onboard_next),
                    getString(R.string.onboard_header4),
                    getString(R.string.onboard_text4),
                    R.drawable.onboard_4
                )
            )
            add(
                OnboardGuideElementModel(
                    getString(R.string.onboard_next),
                    getString(R.string.onboard_header5),
                    getString(R.string.onboard_text5),
                    R.drawable.onboard_5
                )
            )
            add(
                OnboardGuideElementModel(
                    getString(R.string.onboard_begin),
                    getString(R.string.onboard_header6),
                    getString(R.string.onboard_text6),
                    R.drawable.onboard_6
                )
            )
        }
    }

    private fun shouldCloseScreen() {
        onOnboardFinishedListener.onOnboardFinished()
    }

    private fun changeSelectionCircle(position: Int) {
        for (i in 0 until binding.circlesLayout.childCount) {
            (binding.circlesLayout.getChildAt(i) as ImageView)
                .setImageResource(R.drawable.ic_point_deselected)
        }
        (binding.circlesLayout.getChildAt(position) as ImageView)
            .setImageResource(R.drawable.ic_point_selected)
    }

    private fun onStartMainActivity() {
        onboardViewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_START)
        onboardViewModel.shouldCloseScreen()
    }

    private fun onChangePosition(position: Int) {
        onboardViewModel.analyticsHelper.sendEvent(AnalyticsEvent.OB_NEXT)
        onboardViewModel.changeCurrentPosition(position + 1)
    }

    interface OnOnboardFinishedListener {
        fun onOnboardFinished()
    }

    companion object {

        fun newInstance(): OnboardFragment {
            return OnboardFragment()
        }
    }
}
