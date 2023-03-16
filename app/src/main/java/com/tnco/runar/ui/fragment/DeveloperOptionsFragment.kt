package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tnco.runar.ui.layouts.DeveloperOptionsFragmentLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeveloperOptionsFragment : Fragment(), HasVisibleNavBar {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            DeveloperOptionsFragmentLayout(findNavController())
        }
    }
}
