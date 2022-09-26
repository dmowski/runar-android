package com.tnco.runar.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.util.AnalyticsConstants
import com.tnco.runar.util.AnalyticsUtils
import com.tnco.runar.databinding.FragmentLayoutProcessingBinding
import com.tnco.runar.feature.MusicController
import com.tnco.runar.ui.viewmodel.ProcessingViewModel
import kotlinx.coroutines.delay


class LayoutProcessingFragment : Fragment(R.layout.fragment_layout_processing) {

    private var layoutId: Int = 0
    private var userLayout = intArrayOf()

    private var _binding: FragmentLayoutProcessingBinding? = null
    val binding
        get() = _binding!!

    private val viewModel: ProcessingViewModel by viewModels()

    private var link = "http://www.google.com"
    private val args: LayoutProcessingFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutId = args.layoutId
        userLayout = args.userLayout

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val direction = LayoutInterpretationFragmentDirections
                .actionGlobalLayoutFragment()
            findNavController().navigate(direction)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentLayoutProcessingBinding.bind(view)
        progressBarAction()
        viewModel.getLayoutName(layoutId)
        viewModel.layoutName.observe(viewLifecycleOwner) { name ->
            binding.descriptionHeaderFrame.text = name
        }
        viewModel.fontSize.observe(viewLifecycleOwner) { textSize ->
            val headerTextSize = (textSize * 3.0f)
            val buttonTextSize = (textSize * 1.65f)
            val simpleTextSize = (textSize * 0.8f)
            val advertHeaderTextSize = (textSize * 1.2f)
            binding.descriptionHeaderFrame.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerTextSize)
            binding.descriptionButtonFrame.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize)
            binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, simpleTextSize)
            binding.textSongName.setTextSize(TypedValue.COMPLEX_UNIT_PX, simpleTextSize)
            binding.textGroupName.setTextSize(TypedValue.COMPLEX_UNIT_PX, advertHeaderTextSize)
            when (MusicController.currentSongPos) {
                2 -> {
                    link = "https://lyod1.bandcamp.com/releases"
                    binding.textGroupName.text = getString(R.string.group1)
                    binding.textSongName.text = getString(R.string.track1_1)
                    binding.imageGroup.setImageResource(R.drawable.led_image)
                }
                3 -> {
                    link = "https://lyod1.bandcamp.com/releases"
                    binding.textGroupName.text = getString(R.string.group1)
                    binding.textSongName.text = getString(R.string.track1_2)
                    binding.imageGroup.setImageResource(R.drawable.led_image)
                }
                0 -> {
                    link = "https://danheimmusic.com/"
                    binding.textGroupName.text = getString(R.string.group2)
                    binding.textSongName.text = getString(R.string.track2_1)
                    binding.imageGroup.setImageResource(R.drawable.danheim_image)
                }
                1 -> {
                    link = "https://danheimmusic.com/"
                    binding.textGroupName.text = getString(R.string.group2)
                    binding.textSongName.text = getString(R.string.track2_2)
                    binding.imageGroup.setImageResource(R.drawable.danheim_image)
                }
            }

        }

        binding.descriptionButtonFrame.setOnClickListener {
            AnalyticsHelper.sendEvent(
                AnalyticsEvent.MUSIC_LINK_OPENED,
                Pair(AnalyticsConstants.GROUP_NAME, binding.textGroupName.text.toString()),
                Pair(AnalyticsConstants.TRACK_NAME, binding.textSongName.text.toString())
            )
            val uri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun progressBarAction() {
        lifecycleScope.launchWhenResumed {
            for (i in 0..100) {
                binding.progressOfLoadingView.progress = i
                delay(50)
            }
            val layoutName = AnalyticsUtils.convertLayoutIdToName(layoutId)
            AnalyticsHelper.sendEvent(
                AnalyticsEvent.INTERPRETATION_VIEWED,
                Pair(AnalyticsConstants.DRAW_RUNE_LAYOUT, layoutName)
            )
            val direction = LayoutProcessingFragmentDirections
                .actionLayoutProcessingFragmentToLayoutInterpretationFragment(layoutId, userLayout)
            findNavController().navigate(direction)
        }
    }
}

