package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tnco.runar.databinding.LayoutWebShareBinding
import com.tnco.runar.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebShareFragment : Fragment() {

    private var _binding: LayoutWebShareBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = LayoutWebShareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            webShare.webViewClient = WebViewClient()
            webShare.webChromeClient = WebChromeClient()
            webShare.settings.javaScriptEnabled = true
            webShare.settings.loadWithOverviewMode = true
            webShare.loadUrl(viewModel.shareURL)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
