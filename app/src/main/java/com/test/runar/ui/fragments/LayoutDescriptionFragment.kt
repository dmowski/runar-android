package com.test.runar.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.test.runar.R
import com.test.runar.RunarLogger
import com.test.runar.databinding.FragmentLayoutDescriptionBinding
import com.test.runar.extensions.setOnCLickListenerForAll
import com.test.runar.presentation.viewmodel.DescriptionViewModel
import com.test.runar.ui.Navigator

class LayoutDescriptionFragment : Fragment(R.layout.fragment_layout_description), View.OnClickListener {

    private val viewModel: DescriptionViewModel by viewModels()
    private var layoutId: Int = 0
    private var navigator: Navigator? = null

    private var _binding: FragmentLayoutDescriptionBinding? = null
    private val binding
        get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutId = requireArguments().getInt(KEY_LAYOUT_ID)
        viewModel.getLayoutDescription(layoutId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLayoutDescriptionBinding.bind(view)

        val listOfView = listOf(binding.descriptionButtonFrame, binding.exitButton)
        listOfView.setOnCLickListenerForAll(this)

        viewModel.fontSize.observe(viewLifecycleOwner) { textSize ->
            viewModel.selectedLayout.observe(viewLifecycleOwner) { layoutDescriptionModel ->
                if (layoutDescriptionModel != null) {
                    binding.descriptionHeaderFrame.text = layoutDescriptionModel.layoutName
                    binding.descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                    binding.descriptionTextView.text = layoutDescriptionModel.layoutDescription
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        if (binding.checkbox.isChecked) {
            viewModel.notShowSelectedLayout(layoutId)
        }
        when (v?.id) {
            R.id.exit_button -> {
                requireActivity().onBackPressed()
            }
            R.id.description_button_frame -> {
                navigator?.navigateToLayoutInitFragment(layoutId)
            }
        }
    }

    companion object {
        private const val KEY_LAYOUT_ID = "LAYOUT_ID"

        fun newInstance(id: Int): LayoutDescriptionFragment {
            return LayoutDescriptionFragment().apply { arguments = bundleOf(KEY_LAYOUT_ID to id) }
        }
    }
}