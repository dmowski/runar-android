package com.tnco.runar.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tnco.runar.R
import com.tnco.runar.customClasses.InterTagHandler
import com.tnco.runar.databinding.FragmentLayoutSacr1Binding
import com.tnco.runar.databinding.FragmentLayoutSacr2Binding
import com.tnco.runar.presentation.viewmodel.Sacr1ViewModel
import com.tnco.runar.presentation.viewmodel.Sacr2ViewModel
import com.tnco.runar.ui.Navigator

class SacrFragment2  : Fragment(R.layout.fragment_layout_sacr_2), View.OnClickListener {

    private val viewModel: Sacr2ViewModel by viewModels()

    private var tipSize = 2

    private var navigator: Navigator? = null

    private var fontSize: Float = 0f

    private var _binding: FragmentLayoutSacr2Binding? = null
    private val binding
        get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        tipSize = requireArguments().getInt("tip_size")
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLayoutSacr2Binding.bind(view)
        viewModel.fontSize.observe(viewLifecycleOwner) { textSize ->
            fontSize = textSize
            val headerTextSize = (textSize * 3).toFloat()
            val hintTextSize =(textSize*0.6*1.2).toFloat()
            val gpTextSize = (textSize*0.5*1.2).toFloat()
            binding.descriptionHeaderFrame.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerTextSize)
            binding.hintTw.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSize)
            binding.gpTw.setTextSize(TypedValue.COMPLEX_UNIT_PX, gpTextSize)
            binding.hintTw.setTextColor(resources.getColor(R.color.sacr_hint_main))
            val secondFont = ResourcesCompat.getFont(requireContext(), R.font.roboto_bold)
            binding.hintTw.text = Html.fromHtml(
                "${requireContext().resources.getString(R.string.sacr_hint_text)} <font color='#DADADA'><bf>$${tipSize}</bf></font>",
                null,
                InterTagHandler(secondFont!!)
            )

            binding.gpButtonImg.setOnClickListener(this)
        }
    }


    override fun onDetach() {
        navigator = null
        super.onDetach()
    }



    override fun onClick(v: View?) {
        when (v?.id){
            R.id.gp_button_img->{
                navigator?.navigateToSacrFragment3()
            }
        }
    }
}