package com.tnco.runar.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.databinding.FragmentGeneratorProcessingBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.feature.MusicController
import com.tnco.runar.ui.component.dialog.CancelDialog
import com.tnco.runar.ui.viewmodel.MainViewModel
import com.tnco.runar.util.AnalyticsConstants
import kotlinx.coroutines.delay

class GeneratorMagicRune : Fragment() {

    private var _binding: FragmentGeneratorProcessingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private var link = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            CancelDialog(
                requireContext(),
                viewModel.fontSize.value!!,
                "generator_processing",
                getString(R.string.description_generator_popup)
            ) {
                requireActivity().viewModelStore.clear()
                val direction = GeneratorMagicRuneDirections.actionGlobalGeneratorFragment()
                findNavController().navigate(direction)
            }
                .showDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeneratorProcessingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarAction()
        binding.generatorDescriptionButtonFrame.setOnClickListener {
            AnalyticsHelper.sendEvent(
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
                when (MusicController.currentSongPos) {
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

            val direction = GeneratorMagicRuneDirections
                .actionGeneratorMagicRuneToRunePatternGenerator()
            findNavController().navigate(direction)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}