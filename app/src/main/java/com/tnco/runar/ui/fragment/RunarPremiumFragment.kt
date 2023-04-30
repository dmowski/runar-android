package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import com.tnco.runar.ui.layouts.RunarPremiumFragmentLayout
import com.tnco.runar.ui.viewmodel.RunarPremiumViewModel
import com.tnco.runar.util.PurchaseHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunarPremiumFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val purchaseHelper = PurchaseHelper(requireActivity())
        purchaseHelper.billingSetup()

        return ComposeView(requireContext()).apply {
            setContent {
                val purchaseViewModel: RunarPremiumViewModel = viewModel()
                val fontSize by purchaseViewModel.fontSize.observeAsState()

                val buyEnabled by purchaseHelper.buyEnabled.collectAsState(false)
                val consumeEnabled by purchaseHelper.consumeEnabled.collectAsState(false)
                val statusText by purchaseHelper.statusText.collectAsState("")
                val products by purchaseHelper.products.collectAsState(listOf())

                if (products.isNotEmpty()) {
                    RunarPremiumFragmentLayout(
                        navController = findNavController(),
                        fontSize = fontSize ?: 55f,
                        listOfSkus = products,
                        buyEnabled = buyEnabled
                    ) { chosenSku ->
                        try {
                            purchaseHelper.makePurchase(chosenSku)
                            Log.d("TAG_PURCHASE", "onCreateView: $chosenSku")
                        } catch (e: Exception) {
                            Log.d("TAG_PURCHASE", "onCreateView: ${e.message}")
                            Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }
}
