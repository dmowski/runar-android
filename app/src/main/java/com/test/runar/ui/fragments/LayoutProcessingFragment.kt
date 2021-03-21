package com.test.runar.ui.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.TypedValue
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.test.runar.R
import com.test.runar.databinding.FragmentLayoutProcessingBinding
import com.test.runar.presentation.viewmodel.ProcessingViewModel
import com.test.runar.service.MediaService
import com.test.runar.ui.Navigator
import kotlinx.coroutines.delay
import java.util.*

class LayoutProcessingFragment : Fragment(R.layout.fragment_layout_processing) {

    private var layoutId: Int = 0
    private var userLayout = intArrayOf()

    private var navigator: Navigator? = null

    private var _binding: FragmentLayoutProcessingBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: ProcessingViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutId = requireArguments().getInt(KEY_ID)
        userLayout = requireArguments().getIntArray(KEY_USER_LAYOUT) ?: intArrayOf()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentLayoutProcessingBinding.bind(view)
        progressBarAction()
        viewModel.getLayoutName(layoutId)
        viewModel.layoutName.observe(viewLifecycleOwner) { name ->
            binding.descriptionHeaderFrame.text = name
        }
        viewModel.fontSize.observe(viewLifecycleOwner) { textSize ->
            val headerTextSize = (textSize * 3).toFloat()
            val buttonTextSize = (textSize * 1.65).toFloat()
            val simpleTextSize = (textSize * 0.8).toFloat()
            val advertHeaderTextSize = (textSize * 1.2).toFloat()
            binding.descriptionHeaderFrame.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerTextSize)
            binding.descriptionButtonFrame.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize)
            binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, simpleTextSize)
            binding.textSongName.setTextSize(TypedValue.COMPLEX_UNIT_PX, simpleTextSize)
            binding.textGroupName.setTextSize(TypedValue.COMPLEX_UNIT_PX, advertHeaderTextSize)
            var mediaService = MediaService()
            when (mediaService.getRandArrayElement()) {
                1 -> {
                    binding.textGroupName.text = "stan"
                    binding.textSongName.text = "Неведомо, Не страшно - Черная Ладья"
                    binding.imageGroup.setImageResource(R.drawable.led_image)
                }
                2 -> {
                    binding.textGroupName.text = "monster"
                        binding.textSongName.text = "Мать моя сказала"
                        binding.imageGroup.setImageResource(R.drawable.led_image)
                    }
                3 -> {
                        binding.textGroupName.text = "not afraid"
                        binding.textSongName.text = "Kala"
                        binding.imageGroup.setImageResource(R.drawable.danheim_image)
                    }
                   4-> {
                        binding.textGroupName.text = "lose yourself"
                        binding.textSongName.text = "Runar"
                        binding.imageGroup.setImageResource(R.drawable.danheim_image)
                    }
                }

        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDetach() {
        navigator = null
        super.onDetach()
    }

    private fun progressBarAction() {
        lifecycleScope.launchWhenResumed {
            for (i in 0..100){
                binding.progressOfLoadingView.progress = i
                delay(15)
            }
            navigator?.navigateToInterpretationFragment(layoutId, userLayout)
        }
    }

    companion object {
        private const val KEY_ID = "KEY_ID"
        private const val KEY_USER_LAYOUT = "KEY_USER_LAYOUT"

        fun newInstance(id: Int, userLayout: IntArray): LayoutProcessingFragment {
            return LayoutProcessingFragment().apply {
                arguments = bundleOf(
                    KEY_ID to id,
                    KEY_USER_LAYOUT to userLayout
                )
            }
        }
    }
}

