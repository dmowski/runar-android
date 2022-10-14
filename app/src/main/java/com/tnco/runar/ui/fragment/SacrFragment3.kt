package com.tnco.runar.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tnco.runar.R
import com.tnco.runar.databinding.FragmentLayoutSacr3Binding
import com.tnco.runar.ui.viewmodel.Sacr3ViewModel

class SacrFragment3 : Fragment(R.layout.fragment_layout_sacr_3), View.OnClickListener {

    private val viewModel: Sacr3ViewModel by viewModels()

    private var fontSize: Float = 0f

    private var _binding: FragmentLayoutSacr3Binding? = null
    private val binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLayoutSacr3Binding.bind(view)
        viewModel.fontSize.observe(viewLifecycleOwner) { textSize ->
            fontSize = textSize
            val headerTextSize = (textSize * 3)
            with(binding) {
                descriptionHeaderFrame.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    headerTextSize
                )
                cardButton.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    (textSize * 1.4 * 1.2).toFloat()
                )
                resultTw.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    (textSize * 0.8 * 1.2).toFloat()
                )
            }
        }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}