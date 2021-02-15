package com.test.runar.ui.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.databinding.FragmentLayoutDescriptionBinding
import com.test.runar.extensions.setOnCLickListenerForAll
import com.test.runar.presentation.viewmodel.DescriptionViewModel

class LayoutDescriptionFragment : Fragment(R.layout.fragment_layout_description), View.OnClickListener {

    private lateinit var viewModel: DescriptionViewModel
    private var layoutId: Int = 0

    private var _binding: FragmentLayoutDescriptionBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this)[DescriptionViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        layoutId = arguments?.getInt("layoutId")!!
        viewModel.getLayoutDescription(layoutId) //
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
        val navController = findNavController()
        val bundle = bundleOf("layoutId" to layoutId)
        when (v?.id) {
            R.id.exit_button -> {
                if (binding.checkbox.isChecked) viewModel.notShowSelectedLayout(layoutId)
                navController.navigate(R.id.action_layoutDescriptionFragment_to_layoutFragment)
            }
            R.id.description_button_frame -> {
                if (binding.checkbox.isChecked) viewModel.notShowSelectedLayout(layoutId)
                navController.navigate(R.id.action_layoutDescriptionFragment_to_layoutInitFragment, bundle)
            }
        }
    }
}