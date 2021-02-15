package com.test.runar.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ScrollView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.test.runar.R
import com.test.runar.RunarLogger
import com.test.runar.databinding.FragmentLayoutsBinding
import com.test.runar.extensions.setOnCLickListenerForAll
import com.test.runar.presentation.viewmodel.LayoutViewModel
import com.test.runar.ui.Navigator

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
        setupArrows()
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

    private fun setupArrows() {
        val metrics: DisplayMetrics = requireContext().resources.displayMetrics
        val ratio = metrics.heightPixels.toFloat() / metrics.widthPixels.toFloat()

        if (ratio >= 2.1) {
            binding.arrowDown.isVisible = false
        } else {
            binding.arrowDown.isVisible = true

            binding.arrowDown.setOnClickListener {
                binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }

            binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                val isBottomReached = binding.scrollView.children.last().height - binding.scrollView.height - scrollY == 0
                showUpAndHideDownButtons(isBottomReached)
            }

            binding.arrowUp.setOnClickListener {
                binding.scrollView.fullScroll(ScrollView.FOCUS_UP)
            }
        }
    }

    private fun showUpAndHideDownButtons(state: Boolean) {
        with(binding) {
            arrowUp.isVisible = state
            arrowDown.isVisible = !state
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
        viewModel.showStatus.observe(viewLifecycleOwner) { needShowDescription ->
            if (needShowDescription) {
                navigator?.navigateToLayoutDescriptionFragment(idOfRune)
            } else {
                navigator?.navigateToLayoutInitFragment(idOfRune)
            }
        }
    }
}