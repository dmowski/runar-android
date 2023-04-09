package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import com.tnco.runar.ui.layouts.RunarPremiumFragmentLayout
import com.tnco.runar.ui.viewmodel.RunarPremiumViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunarPremiumFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val purchaseViewModel: RunarPremiumViewModel = viewModel()
                val fontSize by purchaseViewModel.fontSize.observeAsState()

                val listOfSkus = purchaseViewModel.listOfSkus

                RunarPremiumFragmentLayout(
                    navController = findNavController(),
                    fontSize = fontSize ?: 55f,
                    listOfSkus = listOfSkus
                ) { choseRate ->
                    Toast.makeText(requireContext(), choseRate.title, Toast.LENGTH_SHORT).show()
                    // TODO when user select rate and click on button
                }
            }
        }
    }
}
