package com.tnco.runar.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tnco.runar.R
import com.tnco.runar.controllers.MusicController
import com.tnco.runar.databinding.FragmentLayoutProcessingBinding
import com.tnco.runar.presentation.viewmodel.ProcessingViewModel
import com.tnco.runar.ui.Navigator
import kotlinx.coroutines.delay


class LayoutProcessingFragment : Fragment(R.layout.fragment_layout_processing) {

    private var layoutId: Int = 0
    private var userLayout = intArrayOf()

    private var navigator: Navigator? = null

    private var _binding: FragmentLayoutProcessingBinding? = null
    val binding
        get() = _binding!!

    private val viewModel: ProcessingViewModel by viewModels()

    private var link = "http://www.google.com"

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
            when (MusicController.currentSongPos) {
                2 -> {
                    link ="https://lyod1.bandcamp.com/releases"
                    binding.textGroupName.text = "Лёдъ"
                    binding.textSongName.text = "Неведомо, Не страшно - Черная Ладья"
                    binding.imageGroup.setImageResource(R.drawable.led_image)
                }
                3 -> {
                    link ="https://lyod1.bandcamp.com/releases"
                    binding.textGroupName.text = "Лёдъ"
                    binding.textSongName.text = "Мать моя сказала"
                    binding.imageGroup.setImageResource(R.drawable.led_image)
                }
                0 -> {
                    link = "https://danheimmusic.com/"
                    binding.textGroupName.text = "Danheim"
                    binding.textSongName.text = "Kala"
                    binding.imageGroup.setImageResource(R.drawable.danheim_image)
                }
                1 -> {
                    link = "https://danheimmusic.com/"
                    binding.textGroupName.text = "Danheim"
                    binding.textSongName.text = "Runar"
                    binding.imageGroup.setImageResource(R.drawable.danheim_image)
                }
            }

        }

        binding.descriptionButtonFrame.setOnClickListener {
            val uri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDetach() {
        navigator = null
        super.onDetach()
    }

    private fun progressBarAction() {
        lifecycleScope.launchWhenResumed {
            for (i in 0..100) {
                binding.progressOfLoadingView.progress = i
                delay(150)
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

