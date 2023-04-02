package com.tnco.runar.ui.fragment

import android.os.Bundle
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
        arguments?.getStringArrayList(ITEM_CHILD_IDS_LIST)
    }
    private val fragmentTitle by lazy {
        arguments?.getString(FRAGMENT_TITLE)
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
        viewModel.analyticsHelper.sendEvent(AnalyticsEvent.LIBRARY_OPENED)

        libraryItemId?.let {
            viewModel.getFilteredLibraryList(it)
        } ?: viewModel.getRuneDataFromDB()

        fragmentTitle?.let {
            viewModel.updateCurrentFragmentTitle(title = it)
        } ?: viewModel.updateCurrentFragmentTitle(
            title = context?.resources?.getString(
                R.string.library_top_bar_header
            ) ?: LibraryViewModel.DEFAULT_TITLE
        )
        val view = ComposeView(requireContext()).apply {
            setContent {
                LibraryBars(findNavController())
            }
        }
        view.isFocusableInTouchMode = true
        view.requestFocus()
        return view
    }

    companion object {

        private const val ITEM_CHILD_IDS_LIST = "item's child ids list"
        private const val FRAGMENT_TITLE = "fragment title"

        fun createArgs(childIdList: ArrayList<String>?, fragmentTitle: String) =
            Bundle().apply {
                putStringArrayList(ITEM_CHILD_IDS_LIST, childIdList ?: arrayListOf())
                putString(FRAGMENT_TITLE, fragmentTitle)
            }
    }
}
