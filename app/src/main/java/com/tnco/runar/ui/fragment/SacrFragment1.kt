package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tnco.runar.R
import com.tnco.runar.databinding.FragmentLayoutSacr1Binding
import com.tnco.runar.ui.viewmodel.Sacr1ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SacrFragment1 : Fragment(R.layout.fragment_layout_sacr_1), View.OnClickListener {

    private val viewModel: Sacr1ViewModel by viewModels()

    private var fontSize: Float = 0f

    private var _binding: FragmentLayoutSacr1Binding? = null
    private val binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLayoutSacr1Binding.bind(view)
        viewModel.fontSize.observe(viewLifecycleOwner) { textSize ->
            fontSize = textSize
            val headerTextSize = (textSize * 3)
            val luckTextSize = (textSize * 0.8 * 1.2).toFloat()
            val firstTextSize = (textSize * 1.2).toFloat()
            val priceTextSize = (textSize * 0.7 * 1.2).toFloat()
            with(binding) {
                descriptionHeaderFrame.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerTextSize)
                sacrLuckTw.setTextSize(TypedValue.COMPLEX_UNIT_PX, luckTextSize)
                sacrFirstTw.setTextSize(TypedValue.COMPLEX_UNIT_PX, firstTextSize)
                mainText.setTextSize(TypedValue.COMPLEX_UNIT_PX, luckTextSize)
                itemHeader1Tw.setTextSize(TypedValue.COMPLEX_UNIT_PX, luckTextSize)
                itemHeader2Tw.setTextSize(TypedValue.COMPLEX_UNIT_PX, luckTextSize)
                itemHeader3Tw.setTextSize(TypedValue.COMPLEX_UNIT_PX, luckTextSize)
                sacrB2Price.setTextSize(TypedValue.COMPLEX_UNIT_PX, priceTextSize)
            }
        }
        binding.sacrificeButtonImg1.setOnClickListener(this)
        binding.sacrificeButtonImg2.setOnClickListener(this)
        binding.sacrificeButtonImg3.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sacrifice_button_img_1 -> {
                val destination = SacrFragment1Directions.actionSacrFragment1ToSacrFragment2(0)
                findNavController().navigate(destination)
            }
            R.id.sacrifice_button_img_2 -> {
                val destination = SacrFragment1Directions.actionSacrFragment1ToSacrFragment2(2)
                findNavController().navigate(destination)
            }
            R.id.sacrifice_button_img_3 -> {
                val destination = SacrFragment1Directions.actionSacrFragment1ToSacrFragment2(5)
                findNavController().navigate(destination)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
