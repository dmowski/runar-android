package com.test.runar.ui.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.test.runar.R
import com.test.runar.databinding.FragmentLayoutDescriptionBinding
import com.test.runar.presentation.viewmodel.MainViewModel

class LayoutDescriptionFragment : Fragment(R.layout.fragment_layout_description), View.OnClickListener {

    private lateinit var model: MainViewModel
    private var fontSize: Float = 0f
    private var layoutId: Int = 0

    private var _binding: FragmentLayoutDescriptionBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLayoutDescriptionBinding.bind(view)

        view.findViewById<FrameLayout>(R.id.description_button_frame).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.exit_button).setOnClickListener(this)


        model.fontSize.observe(viewLifecycleOwner) { textSize ->
            if (textSize != null) {
                fontSize = textSize
                model.selectedLayout.observe(viewLifecycleOwner) { layoutDescriptionModel ->
                    if (layoutDescriptionModel != null) {
                        (binding.descriptionHeaderFrame.children.first() as TextView).text = layoutDescriptionModel.layoutName
                        binding.descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                        binding.descriptionTextView.text = layoutDescriptionModel.layoutDescription
                        layoutId = layoutDescriptionModel.layoutId!!
                    }
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
        when (v?.id) {
            R.id.exit_button -> {
                if (binding.checkbox.isChecked) model.notShowSelectedLayout(layoutId)
                navController.navigate(R.id.action_layoutDescriptionFragment_to_layoutFragment)
            }
            R.id.description_button_frame -> {
                if (binding.checkbox.isChecked) model.notShowSelectedLayout(layoutId)
                navController.navigate(R.id.action_layoutDescriptionFragment_to_layoutInitFragment)
            }
        }
    }
}