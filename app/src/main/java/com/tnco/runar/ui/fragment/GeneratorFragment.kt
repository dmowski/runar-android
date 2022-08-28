package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.databinding.FragmerntLayoutGeneratorBinding
import com.tnco.runar.util.observeOnce
import com.tnco.runar.ui.activity.MainActivity
import com.tnco.runar.ui.viewmodel.MainViewModel
import okhttp3.internal.proxy.NullProxySelector.select


class GeneratorFragment : Fragment(){
    val viewModel: MainViewModel by viewModels()

    private var _binding: FragmerntLayoutGeneratorBinding? = null
    private val binding get() = _binding!!
    private fun openRunPatternView() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentContainer, GeneratorStartFragment())
            ?.commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmerntLayoutGeneratorBinding.inflate(inflater, container, false)
        AnalyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_OPENED)
        viewModel.fontSize.observeOnce(this) {
            binding.tvToolbar.setTextSize(TypedValue.COMPLEX_UNIT_PX, (it * 1.35f))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showBottomBar()

        with(binding) {
            generatorStav.setOnClickListener {
                openRunPatternView()
            }

            formula.setOnClickListener {
                val bottomSheetFragment = BottomSheetFragment(
                    getString(R.string.formula),
                    getString(R.string.popup_runic_formula),
                    ContextCompat.getDrawable(requireContext(), R.drawable.formula)!!,
                    getString(R.string.coming_soon)
                )
                bottomSheetFragment.show(
                    requireActivity().supportFragmentManager,
                    BottomSheetFragment.TAG
                )
                true
            }

            stav.setOnClickListener {
                val bottomSheetFragment = BottomSheetFragment(
                    getString(R.string.stav),
                    getString(R.string.popup_runic_staves),
                    ContextCompat.getDrawable(requireContext(), R.drawable.stav)!!,
                    getString(R.string.coming_soon)
                )
                bottomSheetFragment.show(
                    requireActivity().supportFragmentManager,
                    BottomSheetFragment.TAG
                )
                true
            }
        }
    }
}

