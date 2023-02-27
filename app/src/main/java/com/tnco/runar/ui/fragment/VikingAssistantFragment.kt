package com.tnco.runar.ui.fragment

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.tnco.runar.R
import com.tnco.runar.databinding.FragmentVikingAssistantBinding

class VikingAssistantFragment : Fragment(), HasVisibleNavBar {
    private var _binding: FragmentVikingAssistantBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVikingAssistantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolBar.apply {
            setNavigationIcon(R.drawable.ic_library_back_arrow_2)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        with(binding.webView) {
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    binding.loadingProgress.visibility = VISIBLE
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.loadingProgress.visibility = GONE
                    super.onPageFinished(view, url)
                }
            }
            // enabling JavaScript in WebView(by default false)
            settings.javaScriptEnabled = true

            loadUrl("https://chat-run.vercel.app/")
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
