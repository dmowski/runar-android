package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tnco.runar.R
import com.tnco.runar.ui.activity.MainActivity
import com.tnco.runar.ui.viewmodel.MainViewModel

class WebShareFragment: Fragment() {

    private lateinit var viewModel: MainViewModel
    lateinit var webView: WebView
    lateinit var webHeader: TextView



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return inflater.inflate(R.layout.layout_web_share, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).hideBottomBar()

        webView = view.findViewById(R.id.web_share)
        webHeader = view.findViewById(R.id.web_header)

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.loadUrl(viewModel.shareURL)




    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).showBottomBar()
    }
}