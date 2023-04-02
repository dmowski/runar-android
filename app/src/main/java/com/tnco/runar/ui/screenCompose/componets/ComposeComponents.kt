package com.tnco.runar.ui.screenCompose.componets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tnco.runar.R

@Composable
internal fun AppBar(
    title: String,
    navController: NavController,
    navIcon: @Composable (() -> Unit)? = null,
    navActions: @Composable RowScope.() -> Unit = {},
    showIcon: Boolean = false,
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                if (showIcon) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_library_back_arrow_2),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .clickable(onClick = { navController.popBackStack() })
                    )
                }
                Text(
                    text = title,
                    color = colorResource(id = R.color.library_top_bar_header),
                    fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
                    style = TextStyle(fontSize = 36.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        backgroundColor = colorResource(id = R.color.transparent),
        navigationIcon = navIcon,
        elevation = 0.dp,
        actions = navActions
    )
}
