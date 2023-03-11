package com.tnco.runar.ui.screenCompose.componets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
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
    icon: ImageVector = Icons.Filled.ArrowBack,
    navController: NavController,
    navIcon: @Composable (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = requireNotNull(title),
                    color = colorResource(id = R.color.library_top_bar_header),
                    fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
                    style = TextStyle(fontSize = 48.sp),
                    textAlign = TextAlign.Center
                )
            }
        },
        backgroundColor = colorResource(id = R.color.transparent),
        navigationIcon = navIcon,
        elevation = 0.dp
    )
}
