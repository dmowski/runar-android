package com.tnco.runar.ui.screenCompose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tnco.runar.R
import com.tnco.runar.ui.Navigator
import com.tnco.runar.ui.fragment.MenuFragmentDirections
import com.tnco.runar.ui.fragment.SettingsFragmentDirections
import com.tnco.runar.ui.screenCompose.componets.AppBar

@Composable
fun MenuScreen(navigator: Navigator, navController: NavController) {
    Menu(navController)
}

@Composable
private fun Menu(navController: NavController) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.menu),
                navController = navController,
                showIcon = false
            )
        },
        backgroundColor = colorResource(id = R.color.transparent)
    ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier.size(150.dp),
                        painter = painterResource(id = R.drawable.menu_image),
                        contentDescription = ""
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp, bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SettingsItem(clickAction = {
                        val direction = MenuFragmentDirections
                            .actionMenuFragmentToSettings()
                        navController.navigate(direction)
                    })
                }
                DividerItem()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FavItem(clickAction = {
                        val direction = MenuFragmentDirections
                            .actionMenuFragmentToFavourites()
                        navController.navigate(direction)
                    })
                }
                DividerItem()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AboutItem(clickAction = {
                        val direction = SettingsFragmentDirections
                            .actionSettingsFragmentToAboutAppFragment()
                        navController.navigate(direction)
                    })
                }
                Row {
                    RateAppItem(clickAction = {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(
                                "https://play.google.com/store/apps/details?id=com.tnco.runar"
                            ) // here is the uri  app in google play
                            setPackage("com.android.vending")
                        }
                        context.startActivity(intent)
                    })
                }
            }
        }
    }
}

@Composable
fun AboutItem(clickAction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = clickAction),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(start = 10.dp),
            painter = painterResource(id = R.drawable.ic_about),
            contentDescription = "",
            tint = colorResource(id = R.color.menu_icons)
        )
        Text(
            modifier = Modifier.padding(start = 40.dp),
            text = stringResource(id = R.string.about_app_txt),
            fontSize = 20.sp,
            color = colorResource(id = R.color.onboard_text_color),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            horizontalAlignment = Alignment.End
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_right),
                contentDescription = "",
                tint = colorResource(id = R.color.menu_icons_next)
            )
        }
    }
}

@Composable
fun FavItem(clickAction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = clickAction),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(start = 10.dp),
            painter = painterResource(id = R.drawable.ic_fav),
            contentDescription = "",
            tint = colorResource(id = R.color.menu_icons)
        )
        Text(
            modifier = Modifier.padding(start = 40.dp),
            text = stringResource(id = R.string.library_bar_fav),
            fontSize = 20.sp,
            color = colorResource(id = R.color.onboard_text_color)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            horizontalAlignment = Alignment.End
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_right),
                contentDescription = "",
                tint = colorResource(id = R.color.menu_icons_next)
            )
        }
    }
}

@Composable
fun SettingsItem(clickAction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = clickAction),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(start = 10.dp),
            painter = painterResource(id = R.drawable.ic_settings),
            contentDescription = "",
            tint = colorResource(id = R.color.menu_icons)
        )
        Text(
            modifier = Modifier.padding(start = 40.dp),
            text = stringResource(id = R.string.settings_layout),
            fontSize = 20.sp,
            color = colorResource(id = R.color.onboard_text_color)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            horizontalAlignment = Alignment.End
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_right),
                contentDescription = "",
                tint = colorResource(id = R.color.menu_icons_next)
            )
        }
    }
}

@Composable
fun RateAppItem(clickAction: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(10.dp, 20.dp, 10.dp, 20.dp)
            .size(400.dp, 60.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.settings_review_button))
            .border(
                width = 1.dp, color = colorResource(id = R.color.generator_btns),
                RoundedCornerShape(10.dp)
            )
            .clickable(onClick = clickAction)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Icon(
                modifier = Modifier.padding(end = 10.dp),
                painter = painterResource(id = R.drawable.ic_rate),
                contentDescription = "",
                tint = colorResource(id = R.color.sacr_button_header)
            )
            Text(
                text = stringResource(id = R.string.rate_app_txt),
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.sacr_button_header)
            )
        }
    }
}

@Composable
private fun DividerItem() {
    Divider(
        color = colorResource(id = R.color.settings_divider)
    )
}

@Preview
@Composable
fun MenuPreview() {
    Menu(rememberNavController())
}
