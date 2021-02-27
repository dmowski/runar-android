package com.test.runar.ui.fragments


import androidx.activity.compose.setContent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.tooling.preview.Preview
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
                TopBar()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TopBar()
}

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = "Библиотека")},
        backgroundColor = colorResource(id = R.color.library_top_bar)
    )
}

@Composable
fun FirstMenuItem() {
    Row(
        Modifier
            .aspectRatio(3.8f, true)
            .background(Color.Green)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .weight(16f)
                .background(Color.Blue)
        )
        Row(
            Modifier
                .fillMaxSize()
                .weight(377f)
                .background(Color.Gray)
        ) {

        }
        Box(
            Modifier
                .fillMaxSize()
                .weight(21f)
                .background(Color.Yellow)
        )
    }
}
