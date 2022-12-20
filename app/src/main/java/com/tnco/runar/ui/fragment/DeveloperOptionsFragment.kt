package com.tnco.runar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.tnco.runar.R
import com.tnco.runar.ui.viewmodel.DeveloperOptionsViewModel

class DeveloperOptionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            DeveloperOptionsScreen(findNavController())
        }
    }
}

@Composable
private fun DeveloperOptionsScreen(navController: NavController) {
    val viewModel: DeveloperOptionsViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState(0f)
    val audioDisplaying by viewModel.audioDisplaying.observeAsState(true)
    val devSwitcherStates = viewModel.devSwitcherStates

    @Composable
    fun TopBar() = TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.developer_options_title),
                color = colorResource(id = R.color.library_top_bar_header_2),
                fontFamily = FontFamily(Font(R.font.roboto_medium)),
                style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() })
            )
        },
        backgroundColor = colorResource(id = R.color.library_top_bar),
        navigationIcon = { TopBarIcon(navController) }
    )

    @Composable
    fun Switchers() {
        devSwitcherStates.forEach { switcher ->
            SwitcherMenuItem(
                fontSize = fontSize,
                header = switcher.key,
                checkAction = {
                    viewModel.putSwitcherState(switcher.key, it)
                    if (!it) {
                        viewModel.hideAudioFairyTales()
                    } else {
                        viewModel.showAudioFairyTales()
                    }
                },
                state = devSwitcherStates[switcher.key] ?: false
            ) {
                viewModel.putSwitcherState(
                    switcher.key,
                    devSwitcherStates[switcher.key]?.not() ?: false
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar()
        },
        backgroundColor = colorResource(id = R.color.settings_top_app_bar)
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            Modifier
                .verticalScroll(state = scrollState, enabled = true)
                .padding(
                    start = dimensionResource(id = R.dimen.settings_padding_left),
                    top = dimensionResource(id = R.dimen.settings_padding_top),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            Switchers()
        }
    }
}

@Composable
private fun TopBarIcon(navController: NavController) {
    IconButton(onClick = { navController.popBackStack() }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_library_back_arrow_2),
            tint = colorResource(id = R.color.library_top_bar_fav),
            contentDescription = "arrow"
        )
    }
}

@Composable
private fun SwitcherMenuItem(
    fontSize: Float,
    header: String,
    checkAction: ((Boolean) -> Unit),
    state: Boolean,
    clickAction: () -> Unit
) {
    Row(
        Modifier
            .aspectRatio(7.5f)
            .clickable(onClick = clickAction)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .weight(335f)
        ) {
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = header,
                    color = colorResource(id = R.color.library_item_header),
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    style = TextStyle(fontSize = with(LocalDensity.current) { fontSize.toSp() })
                )
                Switch(
                    checked = state,
                    onCheckedChange = checkAction,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colorResource(id = R.color.switcher_checked_thumb),
                        checkedTrackColor = colorResource(id = R.color.switcher_checked_back),
                        uncheckedThumbColor = colorResource(id = R.color.switcher_unchecked_thumb),
                        uncheckedTrackColor = colorResource(id = R.color.switcher_unchecked_back),
                    )
                )
            }
        }
        Spacer(
            Modifier
                .fillMaxSize()
                .weight(9f)
        )
    }
}
