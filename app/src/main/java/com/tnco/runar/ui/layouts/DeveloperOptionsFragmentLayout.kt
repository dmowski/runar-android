package com.tnco.runar.ui.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tnco.runar.R
import com.tnco.runar.model.DeveloperSwitcher
import com.tnco.runar.ui.viewmodel.DeveloperOptionsViewModel
import com.tnco.runar.ui.widgets.SwitcherMenuItem
import com.tnco.runar.ui.widgets.TopBar

@Composable
fun DeveloperOptionsFragmentLayout(navController: NavController) {
    val viewModel: DeveloperOptionsViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState(0f)
    val devSwitcherStates by viewModel.switchers.asLiveData().observeAsState()

    @Composable
    fun Switchers(switchers: List<DeveloperSwitcher>) {
        switchers.forEach { switcher ->
            SwitcherMenuItem(
                fontSize = fontSize,
                header = switcher.name,
                checkAction = {
                    viewModel.saveSwitcher(switcher.copy(state = !switcher.state))
                },
                state = switcher.state
            ) {
                viewModel.saveSwitcher(switcher.copy(state = !switcher.state))
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(navController = navController, fontSize = fontSize)
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
            devSwitcherStates?.let {
                Switchers(it)
            }
        }
    }
}

@Preview
@Composable
fun DeveloperOptionsFragmentLayoutPreview() {
    DeveloperOptionsFragmentLayout(navController = rememberNavController()) // TODO handle with viewModel
}
