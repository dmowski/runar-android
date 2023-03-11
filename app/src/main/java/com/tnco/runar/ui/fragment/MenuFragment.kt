package com.tnco.runar.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tnco.runar.ui.Navigator
import com.tnco.runar.ui.screenCompose.MenuScreen
import com.tnco.runar.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : Fragment(), HasVisibleNavBar {

    val viewModel: SettingsViewModel by viewModels()
    private var navigator: Navigator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.updateMusicStatus()
        viewModel.updateLocaleData()
        viewModel.updateOnboardingStatus()
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        navigator = context as Navigator
        super.onAttach(context)
    }

    override fun onDetach() {
        navigator = null
        super.onDetach()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = ComposeView(requireContext()).apply {
            setContent {
                setContent {
                    MenuScreen(navigator!!, findNavController())
                }
            }
        }
        return view
    }

    companion object {

        fun newInstance() =
            MenuFragment().apply {
            }
    }
}
