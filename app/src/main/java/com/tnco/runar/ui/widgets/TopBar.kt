package com.tnco.runar.ui.widgets

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tnco.runar.R

@Composable
fun TopBar(navController: NavController, fontSize: Float) = TopAppBar(
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

@Preview
@Composable
fun TopBarPreview() {
    TopBar(navController = rememberNavController(), fontSize = 40f)
}
