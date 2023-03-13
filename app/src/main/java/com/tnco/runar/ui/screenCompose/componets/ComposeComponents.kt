package com.tnco.runar.ui.screenCompose.componets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    showIcon: Boolean = false
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (showIcon) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_library_back_arrow_2),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 15.dp, start = 5.dp)
                            .clickable(onClick = { navController.popBackStack() }),
                        alignment = Alignment.CenterStart
                    )
                }

                Text(
                    text = title,
                    color = colorResource(id = R.color.library_top_bar_header),
                    fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
                    style = TextStyle(fontSize = 48.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
        backgroundColor = colorResource(id = R.color.transparent),
        navigationIcon = navIcon,
        elevation = 0.dp
    )
}
