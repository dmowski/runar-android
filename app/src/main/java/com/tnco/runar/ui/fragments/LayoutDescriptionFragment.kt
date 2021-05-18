package com.tnco.runar.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tnco.runar.R
import com.tnco.runar.controllers.AnalyticsHelper
import com.tnco.runar.databinding.FragmentLayoutDescriptionBinding
import com.tnco.runar.extensions.setOnCLickListenerForAll
import com.tnco.runar.presentation.viewmodel.DescriptionViewModel
import com.tnco.runar.ui.Navigator

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

    override fun onDetach() {
        navigator = null
        super.onDetach()
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
                    binding.descriptionTextView.text = layoutDescriptionModel.layoutDescription+"\n"
                    val headerTextSize = (textSize*3).toFloat()
                    val buttonTextSize = (textSize*1.65).toFloat()
                    val checkBoxTextSize = (textSize*0.8).toFloat()
                    binding.descriptionHeaderFrame.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerTextSize)
                    binding.descriptionButtonFrame.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize)
                    binding.checkboxText.setTextSize(TypedValue.COMPLEX_UNIT_PX, checkBoxTextSize)
                }
            }
        }
        binding.checkboxText.setOnClickListener(this)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.exit_button -> {
                AnalyticsHelper.interruption("layout_description")
                requireActivity().onBackPressed()
            }
            R.id.description_button_frame -> {
                if (binding.checkbox.isChecked) {
                    viewModel.notShowSelectedLayout(layoutId)
                }
                AnalyticsHelper.drawsStarted(layoutId)
                navigator?.navigateToLayoutInitFragment(layoutId)
            }
            R.id.checkbox_text -> {
                binding.checkbox.isChecked = !binding.checkbox.isChecked
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