package com.tnco.runar.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tnco.runar.R
import com.tnco.runar.controllers.AnalyticsHelper
import com.tnco.runar.databinding.FragmentLayoutsBinding
import com.tnco.runar.extensions.setOnCLickListenerForAll
import com.tnco.runar.presentation.viewmodel.LayoutViewModel
import com.tnco.runar.ui.Navigator

class LayoutFragment : Fragment(R.layout.fragment_layouts), View.OnClickListener {

    private val viewModel: LayoutViewModel by viewModels()
    private var navigator: Navigator? = null

    private var _binding: FragmentLayoutsBinding? = null
    private val binding
        get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLayoutsBinding.bind(view)

        setClickListenerOnRuneLayouts()

/*viewModel.fontSize.observe(viewLifecycleOwner){fontSize->
    val namesFontSize=(fontSize*1.6).toFloat()
    binding.firstLayoutTw.setTextSize(TypedValue.COMPLEX_UNIT_PX, namesFontSize)
    binding.secondLayoutTw.setTextSize(TypedValue.COMPLEX_UNIT_PX, namesFontSize)
    binding.thirdLayoutTw.setTextSize(TypedValue.COMPLEX_UNIT_PX, namesFontSize)
    binding.fourthLayoutTw.setTextSize(TypedValue.COMPLEX_UNIT_PX, namesFontSize)
    binding.fifthLayoutTw.setTextSize(TypedValue.COMPLEX_UNIT_PX, namesFontSize)
    binding.sixthLayoutTw.setTextSize(TypedValue.COMPLEX_UNIT_PX, namesFontSize)
    binding.seventhLayoutTw.setTextSize(TypedValue.COMPLEX_UNIT_PX, namesFontSize)
    binding.eightLayoutTw.setTextSize(TypedValue.COMPLEX_UNIT_PX, namesFontSize)
}*/
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        navigator = null
        super.onDetach()
    }

    private fun setClickListenerOnRuneLayouts() {
        with(binding) {
            val listOfView = listOf(
                firstLayout,
                secondLayout,
                thirdLayout,
                fourthLayout,
                fifthLayout,
                sixthLayout,
                seventhLayout,
                eightLayout
            )
            listOfView.setOnCLickListenerForAll(this@LayoutFragment)
        }
    }

    override fun onClick(v: View?) {
        val idOfRune = when (v?.id) {
            R.id.first_layout -> 1
            R.id.second_layout -> 2
            R.id.third_layout -> 3
            R.id.fourth_layout -> 4
            R.id.fifth_layout -> 5
            R.id.sixth_layout -> 6
            R.id.seventh_layout -> 7
            else -> 8
        }
        viewModel.checkDescriptionStatus(idOfRune)
        AnalyticsHelper.drawsSelected(idOfRune)
        viewModel.showStatus.observe(viewLifecycleOwner) { needShowDescription ->
            if (needShowDescription) {
                navigator?.navigateToLayoutDescriptionFragment(idOfRune)
            } else {
                navigator?.navigateToLayoutInitFragment(idOfRune)
            }
        }
    }
}