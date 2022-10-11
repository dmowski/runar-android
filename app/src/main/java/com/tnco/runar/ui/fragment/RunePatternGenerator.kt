package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.databinding.RunePatternGeneratorBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.ui.component.dialog.CancelDialog
import com.tnco.runar.ui.viewmodel.MainViewModel

class RunePatternGenerator : Fragment() {

    private var _binding: RunePatternGeneratorBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private var firstImageWasReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            CancelDialog(
                requireContext(),
                viewModel.fontSize.value!!,
                "rune_pattern_generator",
                getString(R.string.description_generator_popup)
            ) {
                val direction = RunePatternGeneratorDirections.actionGlobalGeneratorFragment()
                findNavController().navigate(direction)
            }
                .showDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RunePatternGeneratorBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnalyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_CREATED)

        viewModel.runeImagesReady.observe(viewLifecycleOwner, Observer {
            if (it && !firstImageWasReady && viewModel.runesImages.size > 0) {
                binding.imageRune?.setImageBitmap(viewModel.runesImages[0])
                binding.nextType?.visibility = View.VISIBLE
            }
        })

        if (viewModel.runesImages.size > 0) {
            binding.imageRune?.setImageBitmap(viewModel.runesImages[0])
            binding.nextType?.visibility = View.VISIBLE
            firstImageWasReady = true
        } else {
            firstImageWasReady = false
            binding.nextType?.visibility = View.INVISIBLE
            binding.imageRune?.setImageResource(R.drawable.generator_hourglass)
        }


        binding.nextType.setOnClickListener {
            AnalyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_NEW_TYPE)
            viewModel.selectedRuneIndex += 1
            if (viewModel.selectedRuneIndex > viewModel.runesImages.size - 1) {
                viewModel.selectedRuneIndex = 0
            }
            binding.imageRune.setImageBitmap(viewModel.runesImages[viewModel.selectedRuneIndex])
        }

        binding.buttonSelect.setOnClickListener {
            val direction = RunePatternGeneratorDirections
                .actionRunePatternGeneratorToGeneratorBackground()
            findNavController().navigate(direction)
        }
    }
}