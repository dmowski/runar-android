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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val audioFeature = true
const val DELAY_FOR_LOADING = 600L

@AndroidEntryPoint
class LibraryFragment : Fragment() {
    val viewModel: LibraryViewModel by viewModels()

    @ExperimentalPagerApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getRuneDataFromDB()
        viewModel.analyticsHelper.sendEvent(AnalyticsEvent.LIBRARY_OPENED)

        val noInternet = getString(R.string.internet_conn_error1)
        val toast = Toast.makeText(requireContext(), noInternet, Toast.LENGTH_SHORT)

        viewModel.isOnline.observeOnce(viewLifecycleOwner) {
            viewModel.errorLoad.observeOnce(viewLifecycleOwner) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(DELAY_FOR_LOADING)
                    if (viewModel.isOnline.value == false && viewModel.errorLoad.value == true) {
                        toast.show()
                    } else {
                        toast.cancel()
                    }
                }
            }
        }

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
}
