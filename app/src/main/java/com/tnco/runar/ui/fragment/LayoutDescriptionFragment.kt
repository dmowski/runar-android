package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.databinding.FragmentLayoutDescriptionBinding
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.ui.viewmodel.DescriptionViewModel
import com.tnco.runar.util.AnalyticsConstants
import com.tnco.runar.util.AnalyticsUtils
import com.tnco.runar.util.setOnCLickListenerForAll
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LayoutDescriptionFragment :
    Fragment(R.layout.fragment_layout_description),
    View.OnClickListener {

    @Inject
    lateinit var preferencesRepository: SharedPreferencesRepository

    private val viewModel: DescriptionViewModel by viewModels()
    private var layoutId: Int = 0
    private val args: LayoutDescriptionFragmentArgs by navArgs()

    private var _binding: FragmentLayoutDescriptionBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutId = args.layoutId
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
                    binding.descriptionTextView.text =
                        layoutDescriptionModel.layoutDescription + "\n"
                    val headerTextSize = (textSize * 3.0f)
                    val buttonTextSize = (textSize * 1.65f)
                    val checkBoxTextSize = (textSize * 0.8f)
                    binding.descriptionHeaderFrame.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        headerTextSize
                    )
                    binding.descriptionButtonFrame.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        buttonTextSize
                    )
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
                AnalyticsHelper.sendEvent(
                    AnalyticsEvent.SCRIPT_INTERRUPTION,
                    Pair(AnalyticsConstants.PAGE, "layout_description")
                )
                findNavController().popBackStack()
            }
            R.id.description_button_frame -> {
                if (binding.checkbox.isChecked) {
                    viewModel.notShowSelectedLayout(layoutId)
                }
                val layoutName = AnalyticsUtils.convertLayoutIdToName(layoutId)
                AnalyticsHelper.sendEvent(
                    AnalyticsEvent.DRAWS_STARTED,
                    Pair(AnalyticsConstants.DRAW_RUNE_LAYOUT, layoutName)
                )
                val direction = LayoutDescriptionFragmentDirections
                    .actionLayoutDescriptionFragmentToLayoutInitFragment(layoutId)
                findNavController().navigate(direction)
            }
            R.id.checkbox_text -> {
                binding.checkbox.isChecked = !binding.checkbox.isChecked
            }
        }
    }
}
