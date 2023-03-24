package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.tnco.runar.R
import com.tnco.runar.enums.AnalyticsEvent
import com.tnco.runar.ui.viewmodel.LibraryViewModel
import com.tnco.runar.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint

const val audioFeature = true

@AndroidEntryPoint
class LibraryFragment : Fragment(), HasVisibleNavBar {

    val viewModel: LibraryViewModel by viewModels()
    private val libraryItemId by lazy {
        arguments?.getStringArrayList(LIBRARY_ITEM_ID)
    }

    override fun onResume() {
        super.onResume()
        val noInternet = getString(R.string.internet_conn_error1)
        viewModel.isOnline.observeOnce(viewLifecycleOwner) { isOnline ->
            viewModel.errorLoad.observeOnce(viewLifecycleOwner) { errorLoad ->
                if (!isOnline && errorLoad) {
                    Toast.makeText(requireContext(), noInternet, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @ExperimentalPagerApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        viewModel.getRuneDataFromDB()
        viewModel.analyticsHelper.sendEvent(AnalyticsEvent.LIBRARY_OPENED)

        val view = ComposeView(requireContext()).apply {
            setContent {
                LibraryBars(findNavController())
            }
        }
        var header = getString(R.string.library_top_bar_header)
        viewModel.lastMenuHeader.observe(viewLifecycleOwner) {
            header = it
        }

        view.isFocusableInTouchMode = true
        view.requestFocus()

        view.setOnKeyListener { _, keyCode, event ->
            var consumed = false
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    viewModel.goBackInMenu()
                    consumed = true
                    if (header == getString(R.string.library_top_bar_header)) consumed = false
                }
            }
            consumed
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryItemId?.let {
            viewModel.getFilteredLibraryList(it)
        }
            ?: viewModel.getRuneDataFromDB()
    }

    companion object {

        private const val LIBRARY_ITEM_ID = "habit item id"
        private const val EMPTY_STRING = ""

        fun createArgs(libraryItemId: ArrayList<String>?) =
            Bundle().apply {
                putStringArrayList(LIBRARY_ITEM_ID, libraryItemId ?: arrayListOf())
            }
    }
}
