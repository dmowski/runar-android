package com.test.runar.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.test.runar.R
import com.test.runar.databinding.FragmentLayoutProcessingBinding
import com.test.runar.presentation.viewmodel.ProcessingViewModel
import com.test.runar.ui.Navigator
import kotlinx.coroutines.delay

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
            binding.nameLayout.text = name
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

