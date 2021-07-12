package com.tnco.runar.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tnco.runar.R
import com.tnco.runar.databinding.FragmentLayoutSacr1Binding
import com.tnco.runar.databinding.FragmentLayoutSacr3Binding
import com.tnco.runar.presentation.viewmodel.Sacr1ViewModel
import com.tnco.runar.presentation.viewmodel.Sacr3ViewModel
import com.tnco.runar.ui.Navigator

class SacrFragment3  : Fragment(R.layout.fragment_layout_sacr_3), View.OnClickListener {

    private val viewModel: Sacr3ViewModel by viewModels()

    private var navigator: Navigator? = null

    private var fontSize: Float = 0f

    private var _binding: FragmentLayoutSacr3Binding? = null
    private val binding
        get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLayoutSacr3Binding.bind(view)
        viewModel.fontSize.observe(viewLifecycleOwner) { textSize ->
            fontSize = textSize
            val headerTextSize = (textSize * 3).toFloat()
            binding.descriptionHeaderFrame.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerTextSize)
            binding.cardButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize*1.4*1.2).toFloat())
            binding.resultTw.setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize*0.8*1.2).toFloat())
        }
    }


    override fun onDetach() {
        navigator = null
        super.onDetach()
    }



    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}