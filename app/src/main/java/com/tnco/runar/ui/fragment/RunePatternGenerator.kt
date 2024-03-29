package com.tnco.runar.ui.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tnco.runar.R
import com.tnco.runar.data.remote.NetworkResult
import com.tnco.runar.databinding.RunePatternGeneratorBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.ui.viewmodel.MainViewModel
import com.tnco.runar.util.InternalDeepLink
import com.tnco.runar.util.OnSwipeTouchListener
import com.tnco.runar.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunePatternGenerator : Fragment() {

    private var _binding: RunePatternGeneratorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private var firstImageWasReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val uri = Uri.parse(
                InternalDeepLink.CancelDialog
                    .withArgs(
                        "${viewModel.fontSize.value!!}",
                        "rune_pattern_generator",
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
        _binding = RunePatternGeneratorBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun nextPattern(incrementer: Int = 1) {
        if (viewModel.runesImages.isEmpty()) {
            return
        }
        viewModel.analyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_NEW_TYPE)
        var nextIndex = viewModel.selectedRuneIndex + incrementer
        val countOfPatterns = viewModel.runesImages.size

        if (nextIndex >= countOfPatterns) {
            nextIndex = 0
        } else if (nextIndex < 0) {
            nextIndex = countOfPatterns - 1
        }

        viewModel.selectedRuneIndex = nextIndex
        binding.imageRune.setImageBitmap(viewModel.runesImages[viewModel.selectedRuneIndex])
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.analyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_PATTERN_CREATED)

        viewModel.isNetworkAvailable.observeOnce(viewLifecycleOwner) { status ->
            if (status) {
                observeRunesImages()
            } else {
                showInternetConnectionError()
            }
        }

        binding.nextType.setOnClickListener {
            nextPattern()
        }

        val applicationContext = requireActivity().applicationContext
        binding.imageRune.setOnTouchListener(object : OnSwipeTouchListener(applicationContext) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                nextPattern()
            }
            override fun onSwipeRight() {
                super.onSwipeLeft()
                nextPattern(-1)
            }
        })

        binding.buttonSelect.setOnClickListener {
            if (viewModel.runesImages.isNotEmpty()) {
                viewModel.cancelChildrenCoroutines()
                val direction = RunePatternGeneratorDirections
                    .actionRunePatternGeneratorToGeneratorBackground()
                findNavController().navigate(direction)
            }
        }
    }

    private fun observeRunesImages() {
        viewModel.runesImagesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    if (response.data?.isNotEmpty() == true && !firstImageWasReady) {
                        firstImageWasReady = true
                        binding.progressBar.visibility = View.GONE
                        binding.imageRune.visibility = View.VISIBLE
                        binding.nextType.visibility = View.VISIBLE
                        binding.buttonSelect.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.generator_button_background_deselected,
                            null
                        )
                        binding.buttonSelect.setTextColor(
                            resources.getColor(R.color.background_next, null)
                        )
                        binding.imageRune.setImageBitmap(viewModel.runesImages[0])
                    }
                }
                is NetworkResult.Error -> {
                    if (!firstImageWasReady) {
                        showInternetConnectionError()
                    }
                }
                is NetworkResult.Loading -> {
                    binding.imageRune.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showInternetConnectionError() {
        viewModel.clearData()
        viewModel.cancelChildrenCoroutines()
        val uri = Uri.parse(
            InternalDeepLink.ConnectivityErrorFragment
                .withArgs("${R.id.generatorStartFragment}")
        )
        findNavController().navigate(uri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
