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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tnco.runar.R
import com.tnco.runar.ui.screenCompose.componets.AppBar
import com.tnco.runar.ui.viewmodel.AboutViewModel
import com.tnco.runar.ui.widgets.AboutText

@Composable
fun AboutAppFragmentLayout(navController: NavController) {
    val viewModel: AboutViewModel = viewModel()
    val fontSize by viewModel.fontSize.observeAsState()

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.library_bar_fav),
                navController = navController,
                showIcon = true
            )
        },
        backgroundColor = colorResource(id = R.color.library_top_bar_2)

    ) {
        val scrollState = rememberScrollState()
        Column(
            Modifier
                .verticalScroll(state = scrollState, enabled = true)
                .padding(all = dimensionResource(id = R.dimen.about_app_padding))
        ) {
            AboutText()
        }
    }
}

@Preview
@Composable
fun AboutAppFragmentLayoutPreview() {
    AboutAppFragmentLayout(navController = rememberNavController()) // TODO handle with viewModel
}
