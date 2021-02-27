package com.test.runar.ui.fragments


import androidx.activity.compose.setContent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.test.runar.R
import com.test.runar.databinding.FragmentLayoutDescriptionBinding
import com.test.runar.databinding.FragmentLibraryBinding
import com.test.runar.presentation.viewmodel.DescriptionViewModel
import com.test.runar.presentation.viewmodel.LibraryViewModel

class LibraryFragment : Fragment() {
/*
    private val viewModel: LibraryViewModel by viewModels()

    private var _binding: FragmentLibraryBinding? = null
    private val binding
        get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentLibraryBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        viewModel.fontSize.observe(viewLifecycleOwner){ textSize->
            binding.itemHeader1.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            binding.itemHeader2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            binding.itemHeader3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            binding.itemHeader4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            binding.itemHeader5.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            val secondTextSize = (textSize*0.8).toFloat()
            binding.itemText1.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondTextSize)
            binding.itemText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondTextSize)
            binding.itemText3.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondTextSize)
            binding.itemText4.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondTextSize)
            binding.itemText5.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondTextSize)
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                Text(text = "Hello world.")
            }
        }
    }
}
