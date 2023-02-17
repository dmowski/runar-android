package com.tnco.runar.ui.screenCompose.componets

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tnco.runar.R

@Composable
internal fun AppBar(
    title: String,
    icon: ImageVector = Icons.Filled.ArrowBack,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = TextStyle(
                    color = colorResource(id = R.color.audio_top_app_bar_header),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_medium))
                )
            )
        },
        backgroundColor = colorResource(id = R.color.audio_top_app_bar),
        navigationIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "Arrow Back",
                tint = colorResource(id = R.color.audio_top_app_bar_header)
            )
        },
        elevation = 0.dp
    )
}
