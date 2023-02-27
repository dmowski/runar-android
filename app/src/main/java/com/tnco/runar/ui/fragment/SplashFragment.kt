package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tnco.runar.databinding.FragmentSplashBinding
import com.tnco.runar.ui.viewmodel.MusicControllerViewModel
import com.tnco.runar.ui.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()
    private val musicControllerViewModel: MusicControllerViewModel by viewModels()

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
    }

    override fun onResume() {
        musicControllerViewModel.updateSplashStatus(true)
        musicControllerViewModel.startMusic()
        super.onResume()
    }

    override fun onPause() {
        musicControllerViewModel.updateSplashStatus(false)
        musicControllerViewModel.softStopMusic()
        super.onPause()
    }

    private fun setupViewModel() {
        viewModel.progress.observe(viewLifecycleOwner) { progress -> updateProgress(progress) }
        viewModel.isOnboardToStart.observe(viewLifecycleOwner) { launchNextFragment(it) }
    }

    private fun updateProgress(progress: Int) {
        binding.loadingProgress.progress = progress
    }

    private fun launchNextFragment(isOnboardToStart: Boolean) {
        val direction = if (isOnboardToStart) {
            SplashFragmentDirections.actionSplashFragmentToOnboardFragment()
        } else {
            SplashFragmentDirections.actionSplashFragmentToRunicDraws()
        }
        findNavController().navigate(direction)
    }
}
