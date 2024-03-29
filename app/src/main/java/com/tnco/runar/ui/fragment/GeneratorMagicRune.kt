package com.tnco.runar.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tnco.runar.R
import com.tnco.runar.databinding.FragmentGeneratorProcessingBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.ui.viewmodel.MainViewModel
import com.tnco.runar.ui.viewmodel.MusicControllerViewModel
import com.tnco.runar.util.AnalyticsConstants
import com.tnco.runar.util.InternalDeepLink
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class GeneratorMagicRune : Fragment() {

    private var _binding: FragmentGeneratorProcessingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val musicControllerViewModel: MusicControllerViewModel by viewModels()
    private var link = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val uri = Uri.parse(
                InternalDeepLink.CancelDialog
                    .withArgs(
                        "${viewModel.fontSize.value!!}",
                        "generator_processing",
                        getString(R.string.description_runic_draws_popup),
                        "${R.id.generatorStartFragment}"
                    )
            )
            findNavController().navigate(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneratorProcessingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarAction()
        binding.generatorDescriptionButtonFrame.setOnClickListener {
            viewModel.analyticsHelper.sendEvent(
                AnalyticsEvent.MUSIC_LINK_OPENED,
                Pair(AnalyticsConstants.GROUP_NAME, binding.generatorTextGroupName.text.toString()),
                Pair(AnalyticsConstants.TRACK_NAME, binding.generatorTextSongName.text.toString())
            )
            val uri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    private fun progressBarAction() {
        viewModel.getRunePattern()
        lifecycleScope.launchWhenResumed {
            for (i in 0..100) {
                binding.generatorProgressOfLoadingView.progress = i
                delay(70)
                when (musicControllerViewModel.currentSongPos()) {
                    2 -> {
                        link = "https://lyod1.bandcamp.com/releases"
                        with(binding) {
                            generatorTextGroupName.text = getString(R.string.group1)
                            generatorTextSongName.text = getString(R.string.track1_1)
                            generatorImageGroup.setImageResource(R.drawable.led_image)
                        }
                    }
                    3 -> {
                        link = "https://lyod1.bandcamp.com/releases"
                        with(binding) {
                            generatorTextGroupName.text = getString(R.string.group1)
                            generatorTextSongName.text = getString(R.string.track1_2)
                            generatorImageGroup.setImageResource(R.drawable.led_image)
                        }
                    }
                    0 -> {
                        link = "https://danheimmusic.com/"
                        with(binding) {
                            generatorTextGroupName.text = getString(R.string.group2)
                            generatorTextSongName.text = getString(R.string.track2_1)
                            generatorImageGroup.setImageResource(R.drawable.danheim_image)
                        }
                    }
                    1 -> {
                        link = "https://danheimmusic.com/"
                        with(binding) {
                            generatorTextGroupName.text = getString(R.string.group2)
                            generatorTextSongName.text = getString(R.string.track2_2)
                            generatorImageGroup.setImageResource(R.drawable.danheim_image)
                        }
                    }
                }
            }

            findNavController().currentBackStackEntryFlow.collect { navBackStackEntry ->
                if (navBackStackEntry.destination.id == R.id.generatorMagicRune) {
                    val direction = GeneratorMagicRuneDirections
                        .actionGeneratorMagicRuneToRunePatternGenerator()
                    findNavController().navigate(direction)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
