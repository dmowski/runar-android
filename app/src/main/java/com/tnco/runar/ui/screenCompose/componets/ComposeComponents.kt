package com.tnco.runar.ui.screenCompose.componets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
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
import com.tnco.runar.ui.component.dialog.SavedLayoutsDialog

@Composable
internal fun AppBar(
    title: String,
    navController: NavController,
    navIcon: @Composable (() -> Unit)? = null,
    navActions: @Composable RowScope.() -> Unit = {},
    showIcon: Boolean = false,
    showDelate: Boolean = false
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxSize(),
                Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
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
                Image(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.Transparent, BlendMode.SrcIn)
                )
            }
        },
        backgroundColor = colorResource(id = R.color.transparent),
        navigationIcon = navIcon,
        elevation = 0.dp,
        actions = navActions
    )
}

@Composable
private fun TopBarActions(fontSize: Float, clickAction: () -> Unit) {
    val context = LocalContext.current
    IconButton(onClick = {
        SavedLayoutsDialog(context, fontSize, clickAction).showDialog()
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_delete),
            tint = colorResource(id = R.color.fav_top_bar_delete),
            contentDescription = "trash"
        )
    }
}
