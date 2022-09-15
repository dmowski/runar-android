package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tnco.runar.R
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.databinding.FragmerntLayoutGeneratorBinding
import com.tnco.runar.util.observeOnce
import com.tnco.runar.ui.activity.MainActivity
import com.tnco.runar.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class GeneratorFragment : Fragment() {
    val viewModel: MainViewModel by viewModels()

    private var _binding: FragmerntLayoutGeneratorBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmerntLayoutGeneratorBinding.inflate(inflater, container, false)
        viewModel.fontSize.observeOnce(this) {
            binding.tvToolbar.setTextSize(TypedValue.COMPLEX_UNIT_PX, (it * 1.35f))
        }

        binding.generatorStav.setOnClickListener {
            checkInternetConnection()
        }

        binding.errorLayout.btnRetry.setOnClickListener {
            hideInternetConnectionError()
        }
        return binding.root
    }

    private fun checkInternetConnection() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.isNetworkAvailable.observeOnce(viewLifecycleOwner) { status ->
                    if (status) {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.fragmentContainer, GeneratorStartFragment())
                            ?.commit()
                    } else {
                        showInternetConnectionError()
                    }
                }
            }
        }
    }

    private fun showInternetConnectionError() = with(binding) {
        generatorStav.visibility = View.GONE
        formula.visibility = View.GONE
        stav.visibility = View.GONE
        textView2.visibility = View.GONE

        errorLayout.errorMessage.visibility = View.VISIBLE
        errorLayout.btnRetry.visibility = View.VISIBLE
    }

    private fun hideInternetConnectionError() = with(binding) {
        generatorStav.visibility = View.VISIBLE
        formula.visibility = View.VISIBLE
        stav.visibility = View.VISIBLE
        textView2.visibility = View.VISIBLE

        errorLayout.errorMessage.visibility = View.GONE
        errorLayout.btnRetry.visibility = View.GONE
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnalyticsHelper.sendEvent(AnalyticsEvent.GENERATOR_OPENED)
        (activity as MainActivity).showBottomBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

